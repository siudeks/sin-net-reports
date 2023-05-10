# usage: 
# to test locally script on namespace dev01 use
# . initshell.sh dev01
# so later on you may use aliases
# k == kubectl --namespace onlexnet-sinnet-dev01
# h == helm --namespace onlexnet-sinnet-dev01
namespace="onlexnet-sinnet-$1"
alias kc="kubectl"
alias k="kc --namespace $namespace"
alias h="helm --namespace $namespace"
