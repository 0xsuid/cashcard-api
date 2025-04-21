package example.cashcard;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    private CashCard findCashCard(Long requestId, Principal principal) {
        return cashCardRepository.findByIdAndOwner(requestId, principal.getName());
    }

    @GetMapping("/{requestId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestId, Principal Principal) {
        CashCard cashCard = findCashCard(requestId, Principal);
        if (cashCard != null) {
            return ResponseEntity.ok(cashCard);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb,
            Principal principal) {
        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();

        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
        Page<CashCard> page = cashCardRepository.findByOwner(
                principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));

        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestId, @RequestBody CashCard cashCardUpdate,
            Principal principal) {
        CashCard cashCard = findCashCard(requestId, principal);
        if (cashCard != null) {
            CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{requestId}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable Long requestId, Principal principal) {
        Boolean cashCardExists = cashCardRepository.existsByIdAndOwner(requestId, principal.getName());
        if (cashCardExists) {
            cashCardRepository.deleteById(requestId);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}
