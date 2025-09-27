# template-api-backend-java-maven-spring-mongo
This is a work in progress and may contain bloat. The project is designed to make starting an API project quicker.

# Setting up your new repo's main branch (two methods)
The first (and simplest) method involves deleting the `.git` file in the repo's dir and re-initialising and force pushing a fresh main branch with no commit history.

As for the second method, I'm not sure but I believe this method doesn't technically delete the git history. It may appear to do so, but the refs can still be found somehow. The above method does fully delete (I think).

In both cases, the following is important to understand:

repo-A is the template repo (aka this repo)

repo-B is your new repo (target repo)

I want to clone repo-A's main branch but without the commit history.

_N.B. This will only work if you are allowed to push directly to main. If not, you will need to push to a new branch, raise a PR and then merge into main._

## Method 1 - Deleting `.git` File (Simplest Method)

### Clone the target repo (repo-B)
```bash
git clone git@github.com:<username>/repo-B
```

Then cd into that new clone repo dir.

### Pull the template repo's branch (repo-A)
While in the target repo's dir, run the following:
```bash
git pull git@github.com:<username>/repo-A <branch_name>
```

This will pull in repo-A's branch into repo-B along with repo-A's commit history.

### Delete Git and Initialise Repo
Run the following command to delete the `.git` file:
```bash
rm -rf .git
```
Once that is deleted, run:
```bash
git init;
git remote add origin git@github.com:<username>/<repo-name>
```

### Commit your code
You will need to re-commit your code to your current branch:
```bash
git add -A;
git commit -am "Initial commit"
```

### Push new branch to repo
You may notice your new branch name is `master`. If you wish to change this to something like `main`, then first you will need to run: 
```bash
git branch -m main
```
After that, you can now force push this branch to the remote repo:
```bash
git push -f origin main
```

## Method 2 - Using Orphan Branch

### Clone the target repo (repo-B)
```bash
git clone git@github.com:<username>/repo-B
```

Then cd into that new clone repo dir.

### Pull the template repo's branch
While in the target repo's dir, run the following:
```bash
git pull git@github.com:<username>/repo-A <branch_name>
```

This will pull in repo-A's branch into repo-B along with repo-A's commit history.

### Remove commit history
While still in repo-B's dir, you need to checkout a new orphan branch (no commit history).
```bash
git checkout --orphan <temporary_orphan_branch_name>
```
This will give you a new branch with no history.

### Commit to orphan branch
While on the orphan branch, commit all files.
```bash
git add -A
```
```bash
git commit -am "Initial commit"
```
If you run ```git log``` you should see one commit in the history titled `Initial commit`.

### Delete main branch
Delete the main branch on your target repo (you will rename your second branch main after this).
```bash
git branch -D main
```

### Rename your second branch to main
```bash
git branch -m main
```

### Push your new main branch
```bash
git push -f origin main
```

### DOCKER
Build image:
```bash
docker build -t <name_of_image>:latest -f ./ecs-image-build/Dockerfile .
```

# Default settings
You will need to change a few defaults for the project to work correctly.
1. The _**group id**_ for Maven needs to change to something appropriate. This needs changing in both the pom and packages (main and test).
2. The _**artifact id**_ will also need to change in the pom.
3. The _**url for the MongoDB connection**_ needs to change - follow instructions on the [Mongo Atlas website](https://account.mongodb.com/account/login).
4. Spring security is disabled by default - this will need to be added to the pom.
5. Dependency versions will not necessarily be up-to-date - ensure this is reviewed when starting a new project by viewing the [Maven Repo](https://mvnrepository.com/).
