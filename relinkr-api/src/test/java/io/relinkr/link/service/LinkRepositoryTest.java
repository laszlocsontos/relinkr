/*
  Copyright [2018-2019] Laszlo Csontos (sole trader)

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package io.relinkr.link.service;

import static io.relinkr.test.Mocks.USER_ID;
import static io.relinkr.test.Mocks.createLink;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import io.relinkr.link.model.Link;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LinkRepositoryTest {

  @Autowired
  private LinkRepository linkRepository;

  private Link link;

  @Before
  public void setUp() throws Exception {
    link = linkRepository.save(createLink());
  }

  @Test
  public void findByUserId() {
    List<Link> links = linkRepository.findByUserId(USER_ID);
    assertThat(links, contains(link));
  }

  @Test
  public void findByUserId_withPageRequest() {
    Page<Link> linkPage = linkRepository.findByUserId(USER_ID, PageRequest.of(0, 10));
    assertEquals(1, linkPage.getTotalElements());
  }

}
