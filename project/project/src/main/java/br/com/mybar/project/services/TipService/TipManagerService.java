package br.com.mybar.project.services.TipService;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.mybar.project.request.TipRequest.TipRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class TipManagerService {

    private static final String FILE_PATH = "config/tipConfig.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public TipRequest loadTipsPercents() throws IOException {
        File file = new File(FILE_PATH);

        if(!file.exists())
        {
            TipRequest defaultTip = new TipRequest();

            defaultTip.setTipPercentDrink(0.0);
            defaultTip.setTipPercentFood(0.0);

            saveTips(defaultTip);
            return defaultTip;
        }

        return mapper.readValue(file, TipRequest.class);
    }

    public void saveTips(TipRequest tipRequest) throws IOException {
        File file = new File(FILE_PATH);

        // garante que a pasta existe
        file.getParentFile().mkdirs();

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, tipRequest);
    }

}
