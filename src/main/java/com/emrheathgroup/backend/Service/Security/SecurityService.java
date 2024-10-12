package com.emrheathgroup.backend.Service.Security;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.emrheathgroup.backend.Repository.Security.SecurityRepository;

@Service
public class SecurityService {

	public static List<Map<String, String>> itemkeys = null;

	private static SecurityRepository securityRepository;

	SecurityService(SecurityRepository securityRepository) {
		SecurityService.securityRepository = securityRepository;
	}

	public static String getItemKeyValue(String key) {
		if (itemkeys == null) {
			itemkeys = securityRepository.getItemKeysValue();
		}
		return itemkeys.stream().filter(m -> key.equalsIgnoreCase(m.get("name"))).map(m -> m.get("value")).findFirst()
				.orElse("");
	}

	public static void refreshItemKeys() {
		itemkeys = securityRepository.getItemKeysValue();
	}

}
