package com.eikona.tech.repository;


import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.entity.Device;



@Repository
public interface DeviceRepository extends DataTablesRepository<Device, Long> {

	Device findByCameraId(String camera_id);
	
}
