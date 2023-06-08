package com.eikona.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.sync.WatchListSync;
@RestController
public class WatchlistController {
	
	@Autowired
	private WatchListSync watchListSync;
	
	@GetMapping("/watch-list-sync")
//	@PreAuthorize("hasAuthority('watchlist_sync')")
	public String areaSync(Model model) {
		String message = null;
		try {
			message = watchListSync.syncWatchlist();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}

}
