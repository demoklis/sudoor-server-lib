package net.gplatform.sudoor.server.security.model.repository;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */


import net.gplatform.sudoor.server.security.model.entity.CredentialUser;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 13-12-25.
 * WARNING: Normally we should not use this class to operate db, pls use UserDetailsManager
 */
public interface CredentialUserRepository extends JpaRepository<CredentialUser, String> {
}
