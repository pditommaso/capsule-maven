/*
 * Capsule
 * Copyright (c) 2014-2016, Parallel Universe Software Co. All rights reserved.
 * 
 * This program and the accompanying materials are licensed under the terms 
 * of the Eclipse Public License v1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package capsule;

import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.repository.RemoteRepository;
import org.junit.Test;
import static org.junit.Assert.*;

public class DependencyManagerTest {
    /*
     * This test suite only contains some very localized unit tests.
     * The DependencyManagerDriver program should be used to test the entire DependencyManager component.
     */
    @Test
    public void testParseDependency() {
        Dependency dep;

        dep = dep("com.acme:foo:1.3:jdk8");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals(0, dep.getExclusions().size());

        dep = dep("com.acme:foo:1.3");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(0, dep.getExclusions().size());

        dep = dep("com.acme:foo");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(0, dep.getExclusions().size());

        dep = dep("com.acme:foo::jdk8");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals(0, dep.getExclusions().size());

        dep = dep("com.acme:foo:1.3:jdk8(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo:1.3(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo::jdk8(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo:[1.2,1.3):jdk8(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("[1.2,1.3)", dep.getArtifact().getVersion());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo:(1,2)(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("(1,2)", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());

        dep = dep("com.acme:foo:(org.apache:log4j,javax.jms:jms-api)");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals(2, dep.getExclusions().size());
        assertEquals("org.apache", exc(dep, 0).getGroupId());
        assertEquals("log4j", exc(dep, 0).getArtifactId());
        assertEquals("javax.jms", exc(dep, 1).getGroupId());
        assertEquals("jms-api", exc(dep, 1).getArtifactId());
    }

    @Test
    public void testParseManagedDependency() {
        Dependency dep;

        dep = mandep("com.acme:foo:war:jdk8:1.3");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("war", dep.getArtifact().getExtension());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals(false, dep.isOptional());
        assertEquals(0, dep.getExclusions().size());

        dep = mandep("com.acme:foo::jdk8:1.3");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getExtension());
        assertEquals("jdk8", dep.getArtifact().getClassifier());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals(false, dep.isOptional());
        assertEquals(0, dep.getExclusions().size());

        dep = mandep("com.acme:foo:war::1.3");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("war", dep.getArtifact().getExtension());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals(false, dep.isOptional());
        assertEquals(0, dep.getExclusions().size());

        dep = mandep("com.acme:foo:::1.3");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("", dep.getArtifact().getExtension());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals("1.3", dep.getArtifact().getVersion());
        assertEquals(false, dep.isOptional());
        assertEquals(0, dep.getExclusions().size());

        dep = mandep("com.acme:foo:war::-");
        assertEquals("com.acme", dep.getArtifact().getGroupId());
        assertEquals("foo", dep.getArtifact().getArtifactId());
        assertEquals("war", dep.getArtifact().getExtension());
        assertEquals("", dep.getArtifact().getClassifier());
        assertEquals("", dep.getArtifact().getVersion());
        assertEquals(true, dep.isOptional());
        assertEquals(0, dep.getExclusions().size());
    }

    @Test
    public void testParseRepo() {
        RemoteRepository repo;

        repo = repo("central");
        assertEquals("central", repo.getId());
        assertEquals("https://repo1.maven.org/maven2/", repo.getUrl());

        repo = repo("central-http");
        assertEquals("central", repo.getId());
        assertEquals("http://repo1.maven.org/maven2/", repo.getUrl());

        repo = repo("jcenter");
        assertEquals("jcenter", repo.getId());
        assertEquals("https://jcenter.bintray.com/", repo.getUrl());

        repo = repo("jcenter-http");
        assertEquals("jcenter", repo.getId());
        assertEquals("http://jcenter.bintray.com/", repo.getUrl());

        repo = repo("local");
        assertEquals("local", repo.getId());
        assertEquals("file:" + MavenUserSettings.getInstance().getRepositoryHome(), repo.getUrl());

        repo = repo("http://foo.com");
        assertEquals("http://foo.com", repo.getId());
        assertEquals("http://foo.com", repo.getUrl());

        repo = repo("foo(http://foo.com)");
        assertEquals("foo", repo.getId());
        assertEquals("http://foo.com", repo.getUrl());
    }

    private static Dependency dep(String desc) {
        return DependencyManager.toDependency(desc, "jar");
    }

    private static Dependency mandep(String desc) {
        return DependencyManager.toManagedDependency(desc);
    }

    private static Exclusion exc(Dependency dep, int i) {
        return dep.getExclusions().toArray(new Exclusion[0])[i];
    }

    private static RemoteRepository repo(String desc) {
        return DependencyManager.createRepoBuilder(desc).build();
    }
}
