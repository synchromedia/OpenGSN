package com.greenstarnetwork.services.cloudmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.greenstarnetwork.services.cloudmanager.OverrideMapType.IdPriority;

/**
 * Custom Mapper for the Override Map
 * 
 * @author Mathieu Lemay (IT)
 * 
 */
public class OverridePrioritiesMapAdapter extends XmlAdapter<OverrideMapType, Map<String, Integer>> {
	@Override
	public OverrideMapType marshal(Map<String, Integer> map) throws Exception {
		return new OverrideMapType(map);
	}

	@Override
	public Map<String, Integer> unmarshal(OverrideMapType type) throws Exception {
		Map map = new HashMap();
		List<IdPriority> priorities = type.getPriorities();
		for (OverrideMapType.IdPriority idPriority : priorities) {
			map.put(idPriority.getId(), idPriority.getPriority());
		}
		return map;
	}
}