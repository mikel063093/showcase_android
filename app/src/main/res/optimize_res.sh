#!/bin/bash
dir="$(dirname "$1")"
cd "$dir" || exit 1
echo $dir
for file in $( find . -name "*.jpg" -o -name "*.png" )
do
	newname=`echo $file | sed s/.jpg$/.webp/ | sed s/.png$/.webp/`
	cwebp -q 80 $file -o $newname
	if((`stat -f %z  "$newname"` > `stat  -f %z "$file"`));then
	    echo "Deleting WebP file that is no smaller than PNG"
	    rm -f $newname
	fi
	if((`stat -f %z  "$newname"` < `stat  -f %z "$file"`));then
	    echo "Deleting PNG file that is  smaller than WebP"
	    rm -f $file
	fi

	# delete the WebP file if it is size 0
	if [[ -f $newname && ! -s $newname ]]; then
	    echo "Deleting empty WebP file"
	    rm -f $newname
	fi	
done

# dir="$(dirname "$1")"
# cd "$dir" || exit 1
# base="$(basename "$1" .png)"

# # create a WebP version of the PNG
# cwebp -q 80 "$base".png -o "$base".webp

# delete the WebP file if it is equal size or larger than the original PNG
