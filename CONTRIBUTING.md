# CONTRIBUTING

> **_NOTE:_**
> For compliance reasons, all pull requests must be submitted with a Jira ID as a part of the pull
> request.
>
> You should include the Jira ID near the beginning of the title for better readability.
>
> For example:
> `[XX-1234]: add statement to CONTRIBUTING.md about including Jira IDs in PR titles`
>
> If there is more than one relevant ticket, include all associated Jira IDs.
>
> For example:
> `[WM-1997] [WM-2002] [WM-2005]: fix for many bugs with the same root cause`
>

This document assumes you've completed the steps located in [README.md](./README.md) regarding setup
and running the service. If you haven't completed those steps yet, please go back and make sure you
can run the service.

## Developer convenience scripts

To help accelerate interacting with this repo,
there are a series of scripts available in the `./scripts` directory.

```mermaid
flowchart LR
  e[[setup]]
  v[develop]
  e --> v
  rc[[run local]]
  v --> rc
  rc -- " integration
  test " --> rc

  subgraph docker / k8s
    b[[build]]
    rd[[run docker]]

    b --> rd

    rd -- " integration
    test " --> rd
  end
  v -- lint/test --> b
```

The double walled boxes represent scripts that are available in the `./scripts` directory.
Each script is outfitted with a help and usage guide and are tied into the build process to ensure the continue to run successfully.

## Developing

This document assumes you've completed the steps located in [README.md](./README.md) regarding setup
and running the service.

### Setting up IntelliJ IDEA

Once you have

## Frequently Asked Questions (FAQ)

### Additional questions

Doug Voet created this repository, based in part on other Terra projects
like [Workspace data service](https://github.com/DataBiosphere/terra-workspace-data-service)
and [Terra data catalog](https://github.com/DataBiosphere/terra-data-catalog).

Additional questions can be directed to
the [#dsp-engineering](https://broadinstitute.slack.com/archives/C1C22V6FN/).
