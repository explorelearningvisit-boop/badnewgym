#!/bin/sh
# Scan all files on /storage/emulated/0 and /storage/emulated/999 (OPPO App Cloner / Dual Apps)
find /storage/emulated/0 /storage/emulated/999 /sdcard 2>/dev/null | while IFS= read -r file; do
    # Check extension or special cache files
    ext="$(echo "$file" | awk -F. '{if (NF>1) print tolower($NF)}')"
    is_img=0
    is_vid=0
    is_cache=0

    case "$ext" in
        jpg|jpeg|png|gif|webp|heic|bmp|dng|raw|tif|tiff|svg)
            is_img=1
            ;;
        mp4|mkv|avi|3gp|mov|webm|flv|ts|m4v|wmv)
            is_vid=1
            ;;
    esac

    base="$(basename "$file")"
    case "$base" in
        *imgcache*|*screennailcache*|*tilecache*|*facecache*)
            is_cache=1
            ;;
    esac

    if [ "$is_img" -eq 1 ] || [ "$is_vid" -eq 1 ] || [ "$is_cache" -eq 1 ]; then
        # Check if hidden: file starts with . or path contains /.
        case "$file" in
            */.*)
                hidden="HIDDEN"
                ;;
            *)
                hidden="VISIBLE"
                ;;
        esac
        type="OTHER"
        if [ "$is_img" -eq 1 ]; then type="PHOTO"; fi
        if [ "$is_vid" -eq 1 ]; then type="VIDEO"; fi
        if [ "$is_cache" -eq 1 ]; then type="CACHE_IMG"; fi

        # Stat file for size, epoch timestamp, and path
        stat -c "%s %Y" "$file" 2>/dev/null | while read -r sz mtime; do
            echo "$type|$hidden|$sz|$mtime|$file"
        done
    fi
done
