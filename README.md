# Terra Java Service template

## Using this repo

```text
PLEASE REMOVE THIS SECTION
```

This repository contains codified best practices for creating a new service/application in the Terra ecosystem.

You can use this repository either via The Broad's backstage installation (preferred) or directly.

### via backstage.io

Navigate your browser to
[backstage.io's component creation wizard](https://backstage.dsp-devops.broadinstitute.org/create?filters%5Bkind%5D=template&filters%5Buser%5D=all)
and follow the prompts.

### Direct usage

To create a new project directly based on this repo.

1. Clone this repo to an empty directoy.
2. Run `rm -rf .git` to clear git
3. Run `git init` to initialize a new git repo with these files
4. Modify files with your project-specific information
    1. Update [`CODEOWNERS` file](.github/CODEOWNERS) with the team that owns this repository
    2. Update [`settings.gradle`](./settings.gradle) `rootProject.name` with the name of your project
    3. Update `env.db.name` in `src/main/resources/application.yml` with your database name.
        - Note, this template project is set up to use a cloud sql database for local development.
        - That's appropriate for most projects, but for applications with frequent queries and/or developers with slow internet connections, a local dockerized postgres may provide a faster development experience.
        - See the local dev resources in [Terra Workspace Data Service](https://github.com/DataBiosphere/terra-workspace-data-service/tree/main/local-dev) for scripts for using local Dockerized postgres.
    4. Run `gradle build` to confirm tests pass
5. Make a commit with the files
6. Write your code!

```text
END: PLEASE REMOVE THIS SECTION
```

<!-- INSERT REPOSITORY DESCRIPTION HERE -->
What does it do?

## Environment setup

The following tools are require to interact with this repository.

- java
- docker

To verify your setup, please execute the following command.

```shell
./scripts/setup
```

After running the `setup`-script,
- all other scripts in the `./scripts` directory should be runnable, and
- you should have a container running using the `-psql` suffix

## Running the service

There are two flavors for running the service, either `local`-ly or in a `docker` container.
Both flavors of execution have been scripted for your convenience.

### Local execution

Running either command will get start your api, and you should be able to access it at:
http://localhost:8080

```shell
./scripts/run local
```

### Docker-ized execution

```shell
./scripts/run docker
```

## Next steps

Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for more information about the process of
contributing code to the service and [DESIGN.md](./DESIGN.md) for a deeper understanding of the
repository's structure and design patterns.
