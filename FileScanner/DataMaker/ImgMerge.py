from PIL import Image
import os

def img_merge(PATH):
    images_list = []
    for file_name in os.listdir(PATH):
        abs_path = os.path.join(PATH, file_name)
        path = abs_path.replace('\\', '/')
        images_list.append(path)

    imgs = [Image.open(i) for i in images_list]

    # If you're using an older version of Pillow, you might have to use .size[0] instead of .width
    # and later on, .size[1] instead of .height
    min_img_width = min(i.width for i in imgs)

    total_height = 0
    for i, img in enumerate(imgs):
        # If the image is larger than the minimum width, resize it
        if img.width > min_img_width:
            imgs[i] = img.resize((min_img_width, int(img.height / img.width * min_img_width)), Image.ANTIALIAS)
        total_height += imgs[i].height

    # I have picked the mode of the first image to be generic. You may have other ideas
    # Now that we know the total height of all of the resized images, we know the height of our final image
    img_merge = Image.new(imgs[0].mode, (min_img_width, total_height))
    y = 0
    for img in imgs:
        img_merge.paste(img, (0, y))

        y += img.height
    merged_path = PATH +  '/merged.jpg'
    img_merge.save(merged_path)
    return merged_path

#img_merge('C:/Users/wonai/mystatus/Doit_program/test1.pdf_dir')