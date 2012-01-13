package com.greenstarnetwork.services.cloudmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.jetty.server.Authentication.User;

@XmlType(name = "OverrideMapType")
@XmlAccessorType(XmlAccessType.FIELD)
public class OverrideMapType {
	// produce a wrapper XML element around this collection
	@XmlElementWrapper(name = "prioritiesList")
	@XmlElement(name = "priorities")
	private List<IdPriority> priorities;

	/**
	  *
	  */
	public OverrideMapType() {
	}

	/**
	 * @param map
	 */
	public OverrideMapType(Map<String, Integer> map) {
		priorities = new ArrayList();
		Set<Entry<String, Integer>> set = map.entrySet();
		for (Entry<String, Integer> idPriority : set) {
			priorities.add(new IdPriority(idPriority.getKey(), idPriority.getValue()));
		}
	}

	/**
	 * @return the users
	 */
	public List<IdPriority> getPriorities() {
		return priorities;
	}

	/**
	  *
	  */
	protected static class IdPriority {
		@XmlElement(name = "id")
		private String id;
		@XmlElement(name = "priority")
		private Integer priority;

		/**
	    *
	    */
		protected IdPriority() {
		}

		/**
		 * @param id
		 * @param user
		 */
		protected IdPriority(String id, Integer priority) {
			this.id = id;
			this.priority = priority;
		}

		/**
		 * @return {@link Integer}
		 */
		protected String getId() {
			return id;
		}

		/**
		 * @return {@link User}
		 */
		protected Integer getPriority() {
			return priority;
		}
	}
}
