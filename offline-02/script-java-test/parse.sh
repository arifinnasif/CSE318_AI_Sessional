#!/bin/bash

echo "file,fcvah1,fcvah2,fcvah3,fcvah4,fcvah5,nofcvah1,nofcvah2,nofcvah3,nofcvah4,nofcvah5" > "bt.csv"

for file in $(ls output_data); do
	fcvah1=$(grep "1,true" "output_data/$file" | awk -F "," '{print $5}')
	fcvah2=$(grep "2,true" "output_data/$file" | awk -F "," '{print $5}')
	fcvah3=$(grep "3,true" "output_data/$file" | awk -F "," '{print $5}')
	fcvah4=$(grep "4,true" "output_data/$file" | awk -F "," '{print $5}')
	fcvah5=$(grep "5,true" "output_data/$file" | awk -F "," '{print $5}')
	nofcvah1=$(grep "1,false" "output_data/$file" | awk -F "," '{print $5}')
	nofcvah2=$(grep "2,false" "output_data/$file" | awk -F "," '{print $5}')
	nofcvah3=$(grep "3,false" "output_data/$file" | awk -F "," '{print $5}')
	nofcvah4=$(grep "4,false" "output_data/$file" | awk -F "," '{print $5}')
	nofcvah5=$(grep "5,false" "output_data/$file" | awk -F "," '{print $5}')
	echo "$file,$fcvah1,$fcvah2,$fcvah3,$fcvah4,$fcvah5,$nofcvah1,$nofcvah2,$nofcvah3,$nofcvah4,$nofcvah5" >> "bt.csv"
done
