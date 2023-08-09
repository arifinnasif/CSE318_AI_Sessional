#!/bin/bash

javac ../src/* -d .
rm output_data/*

# input_files=("d-10-01.txt" "d-10-06.txt" "d-10-07.txt" "d-10-08.txt" "d-10-09.txt" "d-15-01.txt" )
is_fcs=("true" "false")
vah=(1 2 3 4 5)

for infile in $(ls input_files); do
	echo "running on" "$infile"
	outfile="${infile%.txt}-out.txt"
	infile="input_files/$infile"
	outfile="output_data/$outfile"
	echo "vah,fc,time,node_count,bt_count" >> "$outfile"
	for b in "${is_fcs[@]}"; do
		echo "   forward check :" $b
#		for (( i = 1; i <= 5; i++ )); do
		for i in ${vah[@]}; do
			if [[ $i == 2 && $b == "false" || $i == 5 && $b == "false" ]]; then
				continue
			fi
			echo "      vah :" $i
#			trap "kill -2 $p" SIGINT
			timeout 3600 java Test "$infile" "$i" "$b" >> "$outfile"
		done
	done
done
