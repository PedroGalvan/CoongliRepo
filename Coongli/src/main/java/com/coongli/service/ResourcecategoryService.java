package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.security.Authority;
import com.coongli.domain.Resource;
import com.coongli.domain.Resourcecategory;
import com.coongli.domain.User;
import com.coongli.forms.AddResourcesForm;
import com.coongli.forms.AddUsersForm;
import com.coongli.repository.ResourcecategoryRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class ResourcecategoryService {

	// Managed repository
	// -------------------------------------------------------
	@Inject
	private ResourcecategoryRepository resourceCategoryRepository;

	// Supporting services
	// ------------------------------------------------------

	@Inject
	ResourceService resourceService;

	@Inject
	AdminisService adminService;

	// Simpled CRUD methods
	// -----------------------------------------------------

	public Resourcecategory create() {
		Resourcecategory result;
		Collection<User> users;
		Collection<Resource> resources;

		result = new Resourcecategory();
		users = new ArrayList<User>();
		resources = new ArrayList<Resource>();
		result.setHidden(false);
		result.setUsers(users);
		result.setResources(resources);

		return result;
	}

	public Resourcecategory save(Resourcecategory resourceCategory) {
		Assert.notNull(resourceCategory);
		//checkIsAdmin();
		Resourcecategory result;

		result = resourceCategoryRepository.save(resourceCategory);

		return result;
	}

	public Resourcecategory findOne(int resourceCategoryId) {
		Resourcecategory result;

		result = resourceCategoryRepository.findOne(resourceCategoryId);

		return result;
	}

	public Collection<Resourcecategory> findAll() {
		Collection<Resourcecategory> result;

		result = resourceCategoryRepository.findAll();

		return result;
	}

	public Resourcecategory findInvoice() {
		//checkIsAdmin();
		Resourcecategory result;

		result = resourceCategoryRepository.findInvoice();
		Assert.isTrue(result.getUsers().isEmpty());

		return result;
	}

	public Resourcecategory findPlanes() {
		Resourcecategory result;

		result = resourceCategoryRepository.findPlanes();
		Assert.isTrue(result.getUsers().isEmpty());

		return result;
	}

	public Resourcecategory findReport() {
		//checkIsAdmin();
		Resourcecategory result;

		result = resourceCategoryRepository.findReport();
		Assert.isTrue(result.getUsers().isEmpty());

		return result;
	}

	public void delete(Resourcecategory resourceCategory) {
		Assert.notNull(resourceCategory);
		Assert.isTrue(resourceCategory.getResources().isEmpty());
		Assert.isTrue(resourceCategory.getHidden() == false);
		//checkIsAdmin();

		resourceCategoryRepository.delete(resourceCategory);
	}

	// Other business methods ------------------------------------------------

	public Collection<Resourcecategory> findAllAdmin() {
		//checkIsAdmin();
		Collection<Resourcecategory> result;

		result = resourceCategoryRepository.findAll();
		result.remove(findInvoice());
		result.remove(findReport());

		return result;
	}

	public Collection<Resource> resourcesInts(Collection<Integer> resourcesInts) {
		Assert.notNull(resourcesInts);
		Collection<Resource> result;
		Resource aux;

		result = new ArrayList<Resource>();
		for (Integer i : resourcesInts) {
			aux = resourceService.findA(i);
			result.add(aux);
		}
		return result;
	}

	public Resourcecategory addUsers(AddUsersForm addUsersForm) {
		Assert.notNull(addUsersForm);
		Resourcecategory result, resourceCategory;
		Collection<User> users, news;

		resourceCategory = addUsersForm.getResourceCategory();
		news = addUsersForm.getUsers();
		if (addUsersForm.getNewUser() != null
				&& !news.contains(addUsersForm.getNewUser())) {
			news.add(addUsersForm.getNewUser());
		}
		users = resourceCategory.getUsers();
		users.removeAll(news);
		users.addAll(news);
		resourceCategory.setUsers(users);
		result = resourceCategoryRepository.save(resourceCategory);
		return result;
	}

	public Resourcecategory addResources(AddResourcesForm addResourcesForm) {
		Assert.notNull(addResourcesForm);
		Resourcecategory result, resourceCategory;
		Collection<Integer> news;
		Collection<Resource> resources, aux;

		resourceCategory = addResourcesForm.getResourceCategory();
		news = addResourcesForm.getResources();
		if (addResourcesForm.getNewr() != 0
				&& !news.contains(addResourcesForm.getNewr())) {
			news.add(addResourcesForm.getNewr());
		}
		aux = resourcesInts(news);
		resources = resourceService
				.findAllResourcesByCategory(resourceCategory);
		resources.removeAll(aux);
		resources.addAll(aux);
		resourceCategory.setResources(resources);
		for (Resource r : resources) {
			r.setResourcecategory(resourceCategory);
			resourceService.save(r);
		}
		result = resourceCategoryRepository.save(resourceCategory);
		return result;
	}

	public void deleteResources(Resourcecategory resourceCategory,
			Collection<Resource> resources) {
		Assert.notNull(resources);
		Assert.notNull(resourceCategory);

		for (Resource r : resources) {
			r.setResourcecategory(null);
			resourceService.save(r);
		}
	}

	public List<List<Resourcecategory>> agruparCategories(
			List<Resourcecategory> resourceCategories) {
		List<List<Resourcecategory>> result;
		List<Resourcecategory> grupoActual, grupoGuardar;

		grupoActual = new ArrayList<Resourcecategory>();
		result = new ArrayList<List<Resourcecategory>>();
		for (Resourcecategory rc : resourceCategories) {
			if (grupoActual.size() < 3) {
				grupoActual.add(rc);
			} else {
				grupoGuardar = new ArrayList<Resourcecategory>(grupoActual);
				grupoActual = new ArrayList<Resourcecategory>();
				grupoActual.add(rc);
				result.add(grupoGuardar);
			}
		}
		if (!grupoActual.isEmpty()) {
			grupoGuardar = new ArrayList<Resourcecategory>(grupoActual);
			result.add(grupoGuardar);
		}
		return result;
	}

	public void checkIsAdmin() {
		UserAccount principal;
		Authority a;
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();

		Assert.isTrue(principal.getAuthorities().contains(a));
	}

}