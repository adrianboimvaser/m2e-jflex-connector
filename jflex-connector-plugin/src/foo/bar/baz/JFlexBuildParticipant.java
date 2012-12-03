package foo.bar.baz;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class JFlexBuildParticipant extends MojoExecutionBuildParticipant {

    public JFlexBuildParticipant(MojoExecution execution) {
        super(execution, true);
    }

    @Override
    public Set<IProject> build(int kind, IProgressMonitor monitor) throws Exception {
        IMaven maven = MavenPlugin.getMaven();
        BuildContext buildContext = getBuildContext();

        // check if initial generation has been done
        File outputDirectory = maven.getMojoParameterValue(getSession(), getMojoExecution(), "outputDirectory", File.class);
        if (outputDirectory != null && outputDirectory.exists()) {
            return null;
        }

        // Otherwise, execute mojo
        final Set<IProject> result = super.build(kind, monitor);

        // tell m2e builder to refresh generated files
        buildContext.refresh(outputDirectory);
        buildContext.refresh(outputDirectory);

        return result;
    }
}
