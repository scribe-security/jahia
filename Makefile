BOLD := $(shell tput -T linux bold)
PURPLE := $(shell tput -T linux setaf 5)
GREEN := $(shell tput -T linux setaf 2)
CYAN := $(shell tput -T linux setaf 6)
RED := $(shell tput -T linux setaf 1)
RESET := $(shell tput -T linux sgr0)
TITLE := $(BOLD)$(PURPLE)
SUCCESS := $(BOLD)$(GREEN)

define title
    @printf '$(TITLE)$(1)$(RESET)\n'
endef

LONG_VERSION := $(shell mvn -q -Dexec.executable=echo -Dexec.args='$${project.version}' --non-recursive exec:exec);
VERSION := $(shell  mvn -q -Dexec.executable=echo -Dexec.args='$${project.version}' --non-recursive exec:exec | sed 's|-.*||' )


.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "$(BOLD)$(CYAN)%-25s$(RESET)%s\n", $$1, $$2}'

.PHONY: version
version: ## Generate version files
	echo '$(VERSION)' > jahia.version
	cp jahia.version image.tag

.PHONY: build
build: ## Build package - removed unittest
	mvn -U -ntp -s .github/maven.settings.xml -e -Dimage.tag=$(cat image.tag) clean install de.qaware.maven:go-offline-maven-plugin:resolve-dependencies -Pgwt-production,docker  -Dbamboo.buildNumber=0
	
#mvn -U -ntp -s .github/maven.settings.xml -e -Dimage.tag=$(cat image.tag) clean deploy de.qaware.maven:go-offline-maven-plug

in:resolve-dependencies -Pgwt-production,unit-tests,docker -Dbamboo.buildNumber=0 -X


# .PHONY: copy-dep
# copy-deb: ## Copy dependencies to provision artifacts
# 	mvn -B -s .github/maven.settings.xml dependency:copy-dependencies -DexcludeTransitive=true -DincludeScope=provided -DincludeGroupIds=org.jahia.modules -DincludeTypes=jar

# .PHONY: copy-dep
# tree: ## Copy dependencies to provision artifacts
# 	mvn -B -s .github/maven.settings.xml dependency:tree -DexcludeTransitive=false -DincludeTypes=jar


# .PHONY: prepare
# prepare: ## Prepare artifacts
# 	@bash prepare.sh