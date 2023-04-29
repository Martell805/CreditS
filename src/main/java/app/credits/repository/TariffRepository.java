package app.credits.repository;

import app.credits.entity.Tariff;
import app.credits.exception.TariffNotFoundException;
import app.credits.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TariffRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MessageService messageService;

    public List<Tariff> findAll() {
        return jdbcTemplate.query("SELECT * FROM tariffs", BeanPropertyRowMapper.newInstance(Tariff.class));
    }

    public Optional<Tariff> findById(Long id) {
        try {
            Tariff tariff = jdbcTemplate.queryForObject("SELECT * FROM tariffs WHERE id=?", BeanPropertyRowMapper.newInstance(Tariff.class),
                    id
            );
            return Optional.ofNullable(tariff);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Tariff> findByType(String type) {
        try {
            Tariff tariff = jdbcTemplate.queryForObject("SELECT * FROM tariffs WHERE type=?", BeanPropertyRowMapper.newInstance(Tariff.class),
                    type
            );
            return Optional.ofNullable(tariff);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Tariff save(Tariff tariff) {
        jdbcTemplate.update("INSERT INTO tariffs(type, interest_rate) VALUES(?, ?)",
                tariff.getType(),
                tariff.getInterestRate()
        );

        return findByType(tariff.getType()).orElseThrow(
                () -> new TariffNotFoundException(messageService.getMessage("exceptions.tariff_not_found_by_type", tariff.getType()))
        );
    }

    public Tariff update(Tariff tariff) {
        jdbcTemplate.update("UPDATE tariffs SET type=?, interest_rate=? WHERE id=?",
                tariff.getType(),
                tariff.getInterestRate(),
                tariff.getId()
        );

        return findById(tariff.getId()).orElseThrow(
                () -> new TariffNotFoundException(messageService.getMessage("exceptions.tariff_not_found_by_id", tariff.getId()))
        );
    }

    public Tariff delete(Tariff tariff) {
        Tariff oldTariff = findByType(tariff.getType()).orElseThrow(
                () -> new TariffNotFoundException(messageService.getMessage("exceptions.tariff_not_found_by_type", tariff.getType()))
        );

        jdbcTemplate.update("DELETE FROM tariffs WHERE id=?",
                tariff.getId()
        );

        return oldTariff;
    }
}
