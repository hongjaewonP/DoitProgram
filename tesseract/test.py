import pytesseract
import cv2
from PIL import Image
from pytesseract import Output
#import argparse
import os
from pdf2jpg import pdf2jpg

#tesseract 절대경로
pytesseract.pytesseract.tesseract_cmd = R'C:\Program Files\Tesseract-OCR\tesseract'
def pdf_to_jpg(file):
    dest = os.path.dirname(file)
    if not os.path.isdir(dest):
        os.mkdir(dest)
    pdf2jpg.convert_pdf2jpg(file, dest, dpi = 300, pages ='ALL')
pdf_to_jpg('file_path')
#from pdf2image import convert_from_path

#pdftoimage 이용하니까 한글 파일이름을 인식할 수 없음!
#import pdftoimage
#pdfs = r'file_route'
#images = convert_from_path(pdfs, 350)
#for i in range(0, len(images)):
    # Save pages as images in the pdf
#    images[i].save('page' + str(i) + '.jpg', 'JPEG')

# load the original image
img = cv2.imread("test.jpg")
img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
#cv2.imshow('test', img_gray)
img_gray = cv2.fastNlMeansDenoising(img_gray, h = 10, templateWindowSize = 21)
sharpen = cv2.GaussianBlur(img_gray, (0,0), 3)
sharpen = cv2.addWeighted(img_gray, 1.5, sharpen, -0.5, 0)
#threshold 이용해서 이미지 이진화
sharpen = cv2.adaptiveThreshold(sharpen, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 21, 15)
cv2.imshow('final', sharpen)
#cv2.waitKey(0)
#한글과 영어를 같이하면 인식률이 오히려 안좋음(한글만 정상적으로 나옴)
#kor 한 결과와 eng한 결과를 합쳐야 될듯.. 어떻게 하는지 공부할 필요 o - lstm 학습?
config = ('-l kor --oem 3 --psm 4')
print(pytesseract.image_to_string(sharpen, config=config))

custom_config = r'--oem 3 --psm 4'
results = pytesseract.image_to_data(sharpen, output_type=Output.DICT, config=custom_config, lang='kor')
#이미지의 key 값들 출력 - 텍스트 지역 추출에 사용
print(results.keys())

# construct the argument parser and parse the arguments - terminal 이용시 사용 하지만 굳이 사용안해도 될듯
#ap = argparse.ArgumentParser()
#ap.add_argument("-i", "--image", required=True,help="path to input image to be OCR'd")
#ap.add_argument("-c", "--min-conf", type=int, default=0,help="mininum confidence value to filter weak text detection")
#args = vars(ap.parse_args())

total_boxes = len(results['text'])
# loop over each of the individual text localizations
for i in range(0, total_boxes):
	# extract the bounding box coordinates of the text region from
	# the current result
	x = results["left"][i]
	y = results["top"][i]
	w = results["width"][i]
	h = results["height"][i]
	# extract OCR text
	# text localization
	text = results["text"][i]
	conf = int(float(results["conf"][i]))
# filter out weak confidence text localizations
	if w > 50: #args["min_conf"]:
	# display the confidence and text to pycharm terminal
	#	print("Confidence: {}".format(conf))
	#	print("Text: {}".format(text))
	#	print("")
	#opencv를 이용해 text 주위로 영역을 설정
    #text = "".join([c if ord(c) < 128 else "" for c in text]).strip()
         cv2.rectangle(sharpen, (x, y), (x + w, y + h), (0, 255, 0), 2)
#텍스트 영역 - 추출한 텍스트 화면출력
#cv2.putText(sharpen, text, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX,1.2, (0, 0, 255), 3)
#창이 너무 크게 출력되어서 이미지 크기 조정
scale_percent = 30  # percent of original size
width = int(sharpen.shape[1] * scale_percent / 100)
height = int(sharpen.shape[0] * scale_percent / 100)
dim = (width, height)

# resized image
resized = cv2.resize(sharpen, dim, interpolation=cv2.INTER_AREA)
print('Resized Dimensions : ', resized.shape)
cv2.imshow("Resized image", resized)
cv2.waitKey(0)
