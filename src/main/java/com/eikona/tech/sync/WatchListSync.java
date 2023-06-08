package com.eikona.tech.sync;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eikona.tech.constants.ApplicationConstants;
import com.eikona.tech.constants.CorsightDeviceConstants;
import com.eikona.tech.constants.MessageConstants;
import com.eikona.tech.constants.NumberConstants;
import com.eikona.tech.entity.Watchlist;
import com.eikona.tech.util.RequestExecutionUtil;
import com.eikona.tech.repository.WatchlistRepository;

@Component
public class WatchListSync {

	@Autowired
	private WatchlistRepository watchlistRepository;

	@Autowired
	private RequestExecutionUtil requestExecutionUtil;
	
	@Value("${corsight.host.url}")
    private String corsightHost;
	
	@Value("${corsight.poi.port}")
    private String poiPort;

	public String syncWatchlist() {
		try {

			String poiUrl = ApplicationConstants.HTTPS_COLON_DOUBLE_SLASH + corsightHost
					+ ApplicationConstants.DELIMITER_COLON + poiPort;
			String addPoiUrl = CorsightDeviceConstants.POI_API_WATCHLIST_SYNC;

			String responeData = requestExecutionUtil.executeHttpsGetRequest(poiUrl, addPoiUrl);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) jsonParser.parse(responeData);
			JSONObject jsonResponseData = (JSONObject) jsonResponse.get(CorsightDeviceConstants.DATA);
			JSONArray jsonArray = (JSONArray) jsonResponseData.get(CorsightDeviceConstants.WATCHLISTS);
			List<Watchlist> listWatchList = new ArrayList<>();
			for (int i = NumberConstants.ZERO; i < jsonArray.size(); i++) {
				JSONObject currentData = (JSONObject) jsonArray.get(i);

				setAreaObjectFromResponse(listWatchList, currentData);
			}
			watchlistRepository.saveAll(listWatchList);
			return MessageConstants.SYNC_SUCCESSFULLY;
		} catch (Exception e) {
			e.printStackTrace();
			return MessageConstants.SYNC_FAILED;
		}
	}

	private void setAreaObjectFromResponse(List<Watchlist> listWatchList, JSONObject jsonData) {
		Watchlist watchlist = watchlistRepository.findByWatchlistIdAndIsDeletedFalse((String) jsonData.get(CorsightDeviceConstants.WATCHLIST_ID));
		if (null == watchlist) {
			Watchlist watchlistObj = new Watchlist();
			watchlistObj.setName((String) jsonData.get(CorsightDeviceConstants.DISPLAY_NAME));
			watchlistObj.setWatchlist((String) jsonData.get(CorsightDeviceConstants.WATCHLIST_TYPE));
			watchlistObj.setWatchlistId((String) jsonData.get(CorsightDeviceConstants.WATCHLIST_ID));
			listWatchList.add(watchlistObj);

		}
	}

}
