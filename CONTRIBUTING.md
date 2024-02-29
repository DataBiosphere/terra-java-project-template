# CONTRIBUTING

> **_NOTE:_**
> For compliance reasons, all pull requests must be submitted with a Jira ID as a part of the pull request.
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


```mermaid
---
title: SDLC - path to prod
---
flowchart LR

e[[setup]]
v[develop]
e-->v

rv[[run-local]]
v-->rv

subgraph docker / k8s
b[[build]]
r[[run-image]]

b-->r

p[[publish]]
b--"integration
test"-->p
end
v--lint/test-->b

d[[deploy]]
p-->d
```

## Developing

### Setting up IntelliJ IDEA

Once you have

## Frequently Asked Questions (FAQ)

##
