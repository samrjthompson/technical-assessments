#!/bin/bash

while getopts w:d:r:t:b: flag
do
  case "${flag}" in
    w) workingdir=${OPTARG};;
    d) directory=${OPTARG};;
    r) repo=${OPTARG};;
    t) template=${OPTARG};;
    b) branch=${OPTARG};;
    *) echo "Unknown flag used" && exit 1;;
  esac
done

WORKING_DIR="$workingdir"
DIRECTORY="$directory"
REPO="$repo"
TEMPLATE="$template"
BRANCH="$branch"

if [ -z "${WORKING_DIR}" ] || [ -z "${DIRECTORY}" ] || [ -z "${REPO}" ] || [ -z "${TEMPLATE}" ] || [ -z "${BRANCH}" ]
then
  echo
  echo "Missing required argument"
  echo
  echo "Arguments provided:"
  echo "###########################"
  echo Working Directory: "${WORKING_DIR}"
  echo Target Directory: "${DIRECTORY}"
  echo Target Repo: "${REPO}"
  echo Template Repo: "${TEMPLATE}"
  echo Branch: "${BRANCH}"
  echo "###########################"
  exit 1
fi

echo "Starting script to setup new repo from template..." \

cd "${WORKING_DIR}" || echo "Working directory does not exist. Exiting." exit 1;
git clone "${REPO}" "${DIRECTORY}" && \

cd "${DIRECTORY}" || echo "Cloned directory does not exist. Exiting." exit 1;
git pull "${TEMPLATE}" "${BRANCH}" && \

rm -rf .git && \

git init;
git remote add origin "${REPO}" && \

git add -A;
git commit -am "Initial commit" && \

git branch -m main && \
git push -f origin main;

