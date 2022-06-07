import os
from PIL import Image
from pdf2jpg import pdf2jpg

#file = r"C:/Users/wonai/NLPprogram/Extract/testword/testword.pdf" ##pdf 파일

def pdf_to_jpg(file):
    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(file)[0]
    file_name = file_path.split('/')[-1]
    dest = pyfile + "/" + file_name

    imgfile = dest + "/" + file_name + ".pdf_dir"

    if not os.path.isdir(dest):
        os.mkdir(dest)

    pdf2jpg.convert_pdf2jpg(file, dest, dpi = 300, pages ='ALL')

    return imgfile #이미지 파일이 있는 dir 절대 경로. 이대로 img_merge 함수 파라미터로 넘기기.


def img_merge(imgfile):
    images_list = []
    for file_name in os.listdir(imgfile):
        abs_path = os.path.join(imgfile, file_name)
        path = abs_path.replace('\\', '/')
        images_list.append(path)

    imgs = [Image.open(i) for i in images_list]

    min_img_width = min(i.width for i in imgs)

    total_height = 0
    for i, img in enumerate(imgs):
        if img.width > min_img_width:
            imgs[i] = img.resize((min_img_width, int(img.height / img.width * min_img_width)), Image.ANTIALIAS)
        total_height += imgs[i].height

    img_merge = Image.new(imgs[0].mode, (min_img_width, total_height))
    y = 0

    pyfile = os.path.dirname(os.path.abspath(__file__))
    pyfile = pyfile.replace("\\", "/")
    file_path = os.path.splitext(imgfile)[0]
    file_name = file_path.split('/')[-1]
    dest = pyfile + "/" + file_name

    for img in imgs:
        img_merge.paste(img, (0, y))

        y += img.height
    merged_path = dest +  '/merged.jpg'
    img_merge.save(merged_path)

    return merged_path ##merged 이미지의 절대 경로


#print(img_merge(pdf_to_jpg(file)))