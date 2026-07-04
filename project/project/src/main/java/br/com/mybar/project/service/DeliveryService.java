package br.com.mybar.project.service;

import br.com.mybar.project.model.Actors.User;
import br.com.mybar.project.model.DefinedTypes.Itemstatus;
import br.com.mybar.project.model.DefinedTypes.UserType;
import br.com.mybar.project.model.ItemConta;
import br.com.mybar.project.repository.AccountItemRepositoryInterface;
import br.com.mybar.project.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;


@Service
public class DeliveryService {

    @Autowired
    private AccountItemRepositoryInterface accountItemRepository;

    @Autowired
    private UserRepositoryInterface userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * UC8: Auxiliar - Retorna o status dinâmico do item com base nos registros de carimbo de data/hora.
     */
    public Itemstatus obterStatusDinamico(ItemConta item) {
        if (item.getDataEntregaBar() != null) {
            return Itemstatus.ENTREGUE;
        } else if (item.getDataRecebimentoBar() != null) {
            return Itemstatus.EM_PREPARO;
        }
        return Itemstatus.SOLICITADO;
    }

    /**
     * UC8: Altera o estado do item para "Em Preparação" ao registrar o recebimento no Balcão/Bar.
     */
    @Transactional
    public ItemConta iniciarPreparacaoNoBalcao(Long itemId) {
        ItemConta item = accountItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item de conta não encontrado com o ID: " + itemId));

        if (!item.getAtivo()) {
            throw new IllegalStateException("Não é possível preparar um item que foi removido da conta.");
        }

        if (item.getDataRecebimentoBar() != null) {
            throw new IllegalStateException("Este item já teve a preparação iniciada no balcão.");
        }

        item.setDataRecebimentoBar(LocalDate.now());
        item.setHoraRecebimentoBar(LocalTime.now());

        return accountItemRepository.save(item);
    }

    /**
     * UC8: Finaliza a entrega do item no balcão.
     * Exige a validação das credenciais (Código e Senha) de um funcionário do tipo ATENDENTE ou ADMIN.
     */
    @Transactional
    public ItemConta finalizarEntregaNoBalcao(Long itemId, int codigoAtendente, String senhaAtendente) {
        // 1. Validar a existência do Atendente
        User atendente = userRepository.findByCodigo(codigoAtendente)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o código fornecido."));

        // 2. Validar o cargo/permissão do Atendente
        if (atendente.getTipo() != UserType.ATENDENTE_BALCAO && atendente.getTipo() != UserType.ADMIN) {
            throw new SecurityException("Apenas Atendentes de Balcão ou Administradores podem confirmar entregas.");
        }

        // 3. Validar a autenticidade da senha informada no balcão
        if (!passwordEncoder.matches(senhaAtendente, atendente.getSenha())) {
            throw new SecurityException("Senha do atendente inválida.");
        }

        // 4. Buscar e validar o item da conta
        ItemConta item = accountItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item de conta não encontrado com o ID: " + itemId));

        if (!item.getAtivo()) {
            throw new IllegalStateException("Este item está cancelado/inativo.");
        }

        if (item.getDataRecebimentoBar() == null) {
            throw new IllegalStateException("O item precisa ser marcado 'Em Preparação' antes de ser entregue.");
        }

        if (item.getDataEntregaBar() != null) {
            throw new IllegalStateException("Este item já foi entregue.");
        }

        // 5. Atualizar os registros temporais de entrega
        item.setDataEntregaBar(LocalDate.now());
        item.setHoraEntregaBar(LocalTime.now());

        return accountItemRepository.save(item);
    }
}