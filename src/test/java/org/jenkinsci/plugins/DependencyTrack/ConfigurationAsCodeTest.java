package org.jenkinsci.plugins.DependencyTrack;


import io.jenkins.plugins.casc.ConfigurationContext;
import io.jenkins.plugins.casc.ConfiguratorRegistry;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import io.jenkins.plugins.casc.model.CNode;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static io.jenkins.plugins.casc.misc.Util.getUnclassifiedRoot;
import static io.jenkins.plugins.casc.misc.Util.toStringFromYamlFile;
import static io.jenkins.plugins.casc.misc.Util.toYamlString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ConfigurationAsCodeTest {

    @Rule
    public final JenkinsRule r = new JenkinsConfiguredWithCodeRule();

    @Test
    @ConfiguredWithCode("configuration-as-code.yml")
    public void shouldSupportConfigurationAsCode() {
        DependencyTrackPublisher publisher = new DependencyTrackPublisher("materials.bom", "bom", true);
        DependencyTrackPublisher.DescriptorImpl descriptor = publisher.getDescriptor();

        assertEquals("oothai8ooWoopishaikee9Ar", descriptor.getDependencyTrackApiKey());
        assertEquals("http://dependency-track.example.org", descriptor.getDependencyTrackUrl());
        assertEquals(120, descriptor.getDependencyTrackPollingTimeout());
        assertTrue(descriptor.isDependencyTrackAutoCreateProjects());
    }

    @Test
    @ConfiguredWithCode("configuration-as-code.yml")
    public void shouldExportConfigurationAsCode() throws Exception {
        ConfiguratorRegistry registry = ConfiguratorRegistry.get();
        ConfigurationContext context = new ConfigurationContext(registry);
        CNode dependencyTrackPublisher = getUnclassifiedRoot(context).get("dependencyTrackPublisher");

        String exported = toYamlString(dependencyTrackPublisher);

        String expected = toStringFromYamlFile(this, "expected-configuration.yml");

        assertThat(exported, is(expected));
    }
}