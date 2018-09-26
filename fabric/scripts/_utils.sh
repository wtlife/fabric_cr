_wait_seconds () {
    delay=$1
    delay=${delay:-"3"}
    sleep $delay
}

uplog() {
    str="$@"
    printf "\033[032m$str\033[0m\n"
}

uplogd() {
    str="$@"
    str_length=${#str}

    f_blank=$[(79-$str_length)/2-3]
    f_blank=$((${f_blank//.*/+1}))
    b_blank=$[79-3-$f_blank-$str_length-3]

    echo "###############################################################################"
    printf "###"
    printf "%-${f_blank}s" " "
    printf "\033[32m$str\033[0m"
    printf "%-${b_blank}s" " "
    printf "\n"
    #printf "###\n"
    #echo "###############################################################################"
}

uploge() {
    echo
    str="$@"
    str_length=${#str}

    f_blank=$[(79-$str_length)/2-3]
    f_blank=$((${f_blank//.*/+1}))
    b_blank=$[79-3-$f_blank-$str_length-3]

    echo "###############################################################################"
    printf "###"
    printf "%-${f_blank}s" " "
    printf "\033[31m$str\033[0m"
    printf "%-${b_blank}s" " "
    printf "###\n"
    echo "###############################################################################"
}

verify_result () {
    if [ $1 -ne 0 ] ; then
        uploge "$2"
        exit 1
    fi
}
