package app.credits.controller;

import app.credits.entity.Tariff;
import app.credits.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan-service")
@RequiredArgsConstructor
public class TariffController {
    private final TariffService tariffService;

    @GetMapping("/getTariffs")
    public ResponseEntity<List<Tariff>> getAll(){
        return ResponseEntity.ok(tariffService.getAll());
    }

    @GetMapping("tariffs/{id}")
    public ResponseEntity<Tariff> get(@PathVariable Long id){
        return ResponseEntity.ok(tariffService.getById(id));
    }

    @PostMapping("tariffs")
    public ResponseEntity<Tariff> post(@RequestBody Tariff tariff){
        return ResponseEntity.ok(tariffService.add(tariff));
    }

    @PatchMapping("tariffs")
    public ResponseEntity<Tariff> patch(@RequestBody Tariff tariff){
        return ResponseEntity.ok(tariffService.edit(tariff));
    }

    @DeleteMapping("tariffs")
    public ResponseEntity<Tariff> delete(@RequestBody Tariff tariff){
        return ResponseEntity.ok(tariffService.delete(tariff));
    }
}
