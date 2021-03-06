package ma.lnet.boncmd.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.lnet.boncmd.domain.Commande;
import ma.lnet.boncmd.service.CommandeService;
import ma.lnet.boncmd.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Commande.
 */
@RestController
@RequestMapping("/api")
public class CommandeResource {

    private final Logger log = LoggerFactory.getLogger(CommandeResource.class);
        
    @Inject
    private CommandeService commandeService;

    /**
     * POST  /commandes : Create a new commande.
     *
     * @param commande the commande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commande, or with status 400 (Bad Request) if the commande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commandes")
    @Timed
    public ResponseEntity<Commande> createCommande(@RequestBody Commande commande) throws URISyntaxException {
        log.debug("REST request to save Commande : {}", commande);
        if (commande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commande", "idexists", "A new commande cannot already have an ID")).body(null);
        }
        Commande result = commandeService.save(commande);
        return ResponseEntity.created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commandes : Updates an existing commande.
     *
     * @param commande the commande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commande,
     * or with status 400 (Bad Request) if the commande is not valid,
     * or with status 500 (Internal Server Error) if the commande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/commandes")
    @Timed
    public ResponseEntity<Commande> updateCommande(@RequestBody Commande commande) throws URISyntaxException {
        log.debug("REST request to update Commande : {}", commande);
        if (commande.getId() == null) {
            return createCommande(commande);
        }
        Commande result = commandeService.save(commande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commande", commande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commandes : get all the commandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of commandes in body
     */
    @GetMapping("/commandes")
    @Timed
    public List<Commande> getAllCommandes() {
        log.debug("REST request to get all Commandes");
        return commandeService.findAll();
    }

    /**
     * GET  /commandes/:id : get the "id" commande.
     *
     * @param id the id of the commande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commande, or with status 404 (Not Found)
     */
    @GetMapping("/commandes/{id}")
    @Timed
    public ResponseEntity<Commande> getCommande(@PathVariable Long id) {
        log.debug("REST request to get Commande : {}", id);
        Commande commande = commandeService.findOne(id);
        return Optional.ofNullable(commande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commandes/:id : delete the "id" commande.
     *
     * @param id the id of the commande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        log.debug("REST request to delete Commande : {}", id);
        commandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commande", id.toString())).build();
    }

}
