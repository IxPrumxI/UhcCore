# Contributing to UhcCore

Thank you for your interest in contributing to this plugin!
If you have any questions, don't hestitate to ask in the `#dev` channel
on [the Discord server](discord-invite).

[discord-invite]: https://discord.gg/fDCaKMX

## Submitting feature requests and bug reports

If you have an idea for a new feature, or found a bug that needs to be
fixed, you can [create an issue][issue-tracker] at the GitLab repository.
Please be as descriptive as possible and include all relevant information,
such as error messages and/or server logs when submitting a bug report.
You are also welcome to discuss the matter on
[the Discord server][discord-invite], in the `#bugs` or `#suggestions` channel.

[issue-tracker]: https://gitlab.com/uhccore/uhccore/-/issues

## Contributing features and bug fixes

Before you start coding, consider discussing the matter on the Discord server,
or submitting a feature request or bug report (see above) describing what
you want to contribute. You may receive tips about where to start or how to
structure your code, which increases the likelihood of your contribution being
accepted. For simple contributions, like fixing a typo or editing a few lines,
you can skip this step.

**Required software**

- Git, to clone the repository and to submit your contribution.
- A Java Development Kit (JDK), to run the Gradle build tool.
- A code editor with support for Java projects using the Gradle build tool.
  - You should also make sure that your editor has support for
  [EditorConfig](https://editorconfig.org), perhaps by downloading a plugin.
  This will ensure that your code is formatted consistently with the rest
  of the code in the project.

**Recommended steps**

1. [Fork this repository][forking] and clone the fork to your local machine.
2. Open the root directory as a Gradle project in your code editor.
3. Wait for the projects to be imported, and then start coding!
4. When you think you are done, make sure to test your changes.
    - Tip: you can execute the `runServer` Gradle task to automatically
    build the plugin and run it on a Paper server (this task comes
    from the [run-paper][run-paper] Gradle plugin).
5. Commit and push your changes on a new Git branch in your forked repository.
6. [Create a merge request][merging] for your contribution.

*Note: a GitLab "merge request" is analogous to a GitHub "pull request".*

[forking]: https://docs.gitlab.com/ee/user/project/repository/forking_workflow.html
[merging]: https://docs.gitlab.com/ee/user/project/repository/forking_workflow.html#merging-upstream
[run-paper]: https://github.com/jpenilla/run-paper
