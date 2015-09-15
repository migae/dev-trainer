#!/bin/bash

## this monstrosity takes the absolute path produced by fswitch,
## extracts the path relative to the current directory, and uses it to
## construct the target path.

function targetclj {
    P=`pwd`
    ROOT=../ear/build/exploded-app
    MODULE=`basename $P`
    VERSION="0.1.0"
    WEBMOD="${MODULE}-${VERSION}"
    PFX="`pwd`/src/main/clojure/"
    tgt=${1##$PFX}
    tdir=`dirname $tgt`
    tfile=`basename $tgt`
    mkdir -p ${ROOT}/${WEBMOD}/WEB-INF/classes/$tdir
    gcp -rv $1 ${ROOT}/${WEBMOD}/WEB-INF/classes/$tdir
    return 0
}

function targetwebapp {
    P=`pwd`
    ROOT=../ear/build/exploded-app
    MODULE=`basename $P`
    VERSION="0.1.0"
    WEBMOD="${MODULE}-${VERSION}"
    PFX="`pwd`/src/main/webapp/"
    echo "PFX: $PFX"
    tgt=${1##$PFX}
    echo "tgt $tgt"
    tdir=`dirname $tgt`
    tfile=`basename $tgt`
    echo "tdir $tdir"
    echo "tfile $tfile"
    echo "res: ${ROOT}/${WEBMOD}/$tdir"
    mkdir -p ${ROOT}/${WEBMOD}/$tdir
    gcp -rv $1 ${ROOT}/${WEBMOD}/$tdir
    return 0
}

export -f targetclj
export -f targetwebapp

## watch clojure source
fswatch -0 -e ".*" -i ".*clj$" \
	--event Updated \
	--recursive \
	src/main/clojure \
	--format %p \
    | xargs -0 -n 1 -I {} \
	    bash -c 'targetclj "$@"' - {} &

## watch webapp  files
fswatch -0 -e ".*~$" \
	--event Updated \
	--recursive \
	src/main/webapp \
	--format %p \
    | xargs -0 -n 1 -I {} \
	    bash -c 'targetwebapp "$@"' - {} &
