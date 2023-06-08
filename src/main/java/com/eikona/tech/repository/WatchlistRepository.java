package com.eikona.tech.repository;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.entity.Watchlist;

@Repository
public interface WatchlistRepository extends DataTablesRepository<Watchlist, Long> {

	Watchlist findByWatchlistIdAndIsDeletedFalse(String string);

	Watchlist findByName(String name);
	


}
