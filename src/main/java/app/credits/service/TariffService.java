package app.credits.service;

import app.credits.exception.TariffNotFoundException;
import app.credits.entity.Tariff;
import app.credits.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;
    private final MessageService messageService;

    public Tariff getById(Long id) {
        return tariffRepository.findById(id).orElseThrow(
                () -> new TariffNotFoundException(messageService.getMessage("exceptions.tariff_not_found_by_id", id))
        );
    }

    public List<Tariff> getAll() {
        return tariffRepository.findAll();
    }
    
    public Tariff add(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    public Tariff edit(Tariff tariff) {
        return tariffRepository.update(tariff);
    }

    public Tariff delete(Tariff tariff) {
        tariffRepository.delete(tariff);
    
        return tariff;
    }
}
