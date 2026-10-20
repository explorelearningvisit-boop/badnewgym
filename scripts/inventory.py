import os
import datetime

def main():
    base_dir = r"c:\Users\User\AndroidStudioProjects\badnewgym\pulled_screens"
    print("================================================================================")
    print("               DETAILED INVENTORY OF PULLED DATA ON LAPTOP                      ")
    print("================================================================================\n")

    grand_total_files = 0
    grand_total_size = 0
    categories = {}

    for root, dirs, files in os.walk(base_dir):
        media_files = [f for f in files if f.lower().endswith(('.png', '.jpg', '.jpeg', '.webp', '.raw', '.mp4', '.csv', '.pdf'))]
        if media_files:
            total_size = sum(os.path.getsize(os.path.join(root, f)) for f in media_files)
            grand_total_files += len(media_files)
            grand_total_size += total_size
            rel = os.path.relpath(root, base_dir).replace("\\", "/")
            mtimes = [os.path.getmtime(os.path.join(root, f)) for f in media_files]
            min_date = datetime.datetime.fromtimestamp(min(mtimes)).strftime('%Y-%m-%d %H:%M')
            max_date = datetime.datetime.fromtimestamp(max(mtimes)).strftime('%Y-%m-%d %H:%M')
            
            # Group by top device/category
            top_cat = rel.split('/')[0] if '/' in rel else "Root"
            if top_cat not in categories:
                categories[top_cat] = []
                
            categories[top_cat].append({
                "rel": rel,
                "count": len(media_files),
                "size_mb": total_size / (1024*1024),
                "min_date": min_date,
                "max_date": max_date,
                "samples": sorted(media_files)[:3]
            })

    for cat, items in categories.items():
        cat_count = sum(i['count'] for i in items)
        cat_size = sum(i['size_mb'] for i in items)
        print(f"=== Category / Device: [{cat}] ({cat_count} files, {cat_size:.2f} MB) ===")
        for item in items:
            folder_label = item['rel'] if item['rel'] != '.' else 'pulled_screens (root)'
            print(f"  [Folder] {folder_label}")
            print(f"    - Files: {item['count']} | Size: {item['size_mb']:.2f} MB | Modified: {item['min_date']} -> {item['max_date']}")
            print(f"    - Sample files: {', '.join(item['samples'])}")
        print()

    print("================================================================================")
    print(f" GRAND TOTAL IN LAPTOP REPOSITORY: {grand_total_files} files | {grand_total_size / (1024*1024):.2f} MB ({grand_total_size / (1024*1024*1024):.3f} GB)")
    print("================================================================================")

if __name__ == "__main__":
    main()
