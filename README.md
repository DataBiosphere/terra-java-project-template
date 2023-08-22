## ${{values.name}}

### Mission
This repository contains codified best practices for creating a new service/application in the Terra ecosystem.

### Using this repo
To create a new project based on this repo
1. Clone this repo to an empty directoy.
2. run `rm -rf .git` to clear git
3. run `git init` to initialize a new git repo with these files
4. make a commit with the files
5. Modify files with your project-specific information
    1. Update `settings.gradle` rootProject.name with the name of your project
    2. Update `env.db.name` in `src/main/resources/application.yml` with your database name.  Note, this template project is set up to use a cloud sql database for local development.  That's appropriate for most projects, but for applications with frequent queries and/or developers with slow internet connections, a local dockerized postgres may provide a faster development experience.  See the local dev resources in (Terra Workspace Data Service)[https://github.com/DataBiosphere/terra-workspace-data-service/tree/main/local-dev] for scripts for using local Dockerized postgres.
    3. Run `gradle build` to confirm tests pass
6. Write your code!



### Authors / questions
Doug Voet created this repository, based in part on other Terra projects like [Workspace Data service](https://github.com/DataBiosphere/terra-workspace-data-service) and [Terra data catalog](https://github.com/DataBiosphere/terra-data-catalog). 

Devon Bush created the first draft of this readme in the process of adapting this repo to create the (still-unnamed) DDP application at https://github.com/broadinstitute/pearl
