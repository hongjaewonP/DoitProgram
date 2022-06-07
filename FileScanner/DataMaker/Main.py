import FileTransformer
import ImgMerge
import KorImgTextExtractor
import DataExtractor
import KorTextPreprocessor



PATH = 'C:/Users/wonai/mystatus/Doit_program/testfile.pdf'

imgPATH = FileTransformer.pdf_to_jpg(PATH) #이미지 파일 경로.
merged_imgPATH = ImgMerge.img_merge(imgPATH) #merged된 이미지 파일 경로
text_bound_list = KorImgTextExtractor.extract_txt_from_img(merged_imgPATH) #최초 text bound list

std = DataExtractor.standard_location(text_bound_list)
info_list = DataExtractor.extract_information(text_bound_list, std)
string_list = DataExtractor.make_string(info_list)

preprocessed_kor_list = KorTextPreprocessor.kor_all_preprocessing(string_list)
print(preprocessed_kor_list)