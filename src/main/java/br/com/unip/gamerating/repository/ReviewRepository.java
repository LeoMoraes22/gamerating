package br.com.unip.gamerating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unip.gamerating.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
