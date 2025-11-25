package com.ShopSphere.shop_sphere.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ShopSphere.shop_sphere.dto.LocationDto;
import com.ShopSphere.shop_sphere.model.Location;
import com.ShopSphere.shop_sphere.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
	private final LocationService locationService;
	
	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}
	
	private Location dtoToEntity(LocationDto dto) {
		Location loc = new Location();
		loc.setLocationId(dto.getLocationId());
		loc.setCity(dto.getCity());
		loc.setHubValue(dto.getHubValue());
		return loc;
	}
	
	private LocationDto entityToDto(Location loc) {
		LocationDto dto = new LocationDto();
		dto.setLocationId(loc.getLocationId());
		dto.setCity(loc.getCity());
		dto.setHubValue(loc.getHubValue());
		return dto;
	}
	
	@PostMapping
	public LocationDto creatLocation(@RequestBody LocationDto dto) {
		Location saved = locationService.createLocation(dtoToEntity(dto));
		return entityToDto(saved);
	}
	
	@GetMapping("/{locationId}")
	public LocationDto getLocationbyId(@PathVariable int locationId) {
		Location loc = locationService.getLocationById(locationId);
		return entityToDto(loc);
	}
	
	@GetMapping
	public List<LocationDto> getAllLocations(){
		return locationService.getAllLocation().stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
}

@GetMapping("/city/{city}")
public List<LocationDto> getAllLocationdsByCity(@PathVariable String city){
return locationService.getLocationsByCity(city)
				.stream()
				.map(this::entityToDto)
				.collect(Collectors.toList());
}

@GetMapping("/search/{keyword}")
public List<LocationDto> searchLocationByKeyword(@PathVariable String keyword){
return locationService.searchLocationByKeyword(keyword)
					.stream()
					.map(this::entityToDto)
					.collect(Collectors.toList());
}


//@PutMapping("/{locationId}")
//public LocationDto updateLocation(
//@PathVariable int locationId,
//@RequestBody LocationDto dto) {
//Location updated = locationService.updateLocation(locationId, dtoToEntity(dto));
//return entityToDto(updated);
//}

@GetMapping("/exists/{city}")
public boolean existsByCity(@PathVariable String city) {
return locationService.existsBycity(city);
}

@GetMapping("/count")
public int countLocations() {
	return locationService.countLocations();
}



}

