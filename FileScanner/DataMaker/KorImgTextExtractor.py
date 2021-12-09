#path = 'C:/Users/wonai/mystatus/Doit_program/(2021-1)의료사회복지론 강의계획안(2차수정).pdf_dir/0_(2021-1)의료사회복지론 강의계획안(2차수정).pdf.jpg'
#C:\Users\wonai\mystatus\Doit_program\FileScanner\venv\lib\site-packages\vision-1.0.0-py3.8-nspkg.pth 삭제 필수

def extract_txt_from_img(file_name):
    from google.cloud import vision
    import io
    client = vision.ImageAnnotatorClient()

    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()

    image = vision.Image(content=content)

    response = client.text_detection(image=image)
    texts = response.text_annotations

    text_list =[]
    #text_list 하나에는 한 jpg 파일의 모든 [단어, 단어bound] 원소쌍이 들어있다.
    for text in texts:
        word_list =[]
        #print('\n"{}"'.format(text.description))
        word_list.append(text.description)
        vertices = (['({},{})'.format(vertex.x, vertex.y)
                    for vertex in text.bounding_poly.vertices])
        word_list.append(vertices)
        #print('bounds: {}'.format(','.join(vertices)))
        text_list.append(word_list)

    if response.error.message:
        raise Exception(
            '{}\nFor more info on error messages, check: '
            'https://cloud.google.com/apis/design/errors'.format(
                 response.error.message))

    return text_list
    #파일의 모든 단어와 단어의 bound를 list에 담아 리턴한다.


#print(extract_txt_from_img('C:/Users/wonai/mystatus/Doit_program/test1.pdf_dir/merged.jpg'))