#1. 띄어쓰기
#2. 전처리

#description -> 요약해서 저징
#prerequisites -> 그냥 저장
#format -> 그냥 저장
#objectives
#evaluation -> 하....
#required -> 그냥 저장
#supplymentary -> 그냥 저장
#optoinal
#policies

from gensim.summarization.summarizer import summarize
word_list = [['Description', "Essential English is the first-level English language course at Ewha. This course will provide instruction in all four skills: reading, writing, speaking and listening, with a particular emphasis on reading and writing: Listening: Students will listen to English of native speakers via their instructor and other media. They will also listen to English spoken by their peers. They will practice focusing on the speaker's message in order to comprehend and produce a meaningful response. Speaking: Students will practice speaking in English through class and small group discussions with their peers. Students may also be asked to complete assignments and projects that will require them to use English outside the classroom and present information to the entire class. Reading: Students will read a variety of texts, including biographical writings and news reports. Through these readings, students will develop basic reading comprehension and critical thinking skills with guidance from the instructor and through discussions with their peers. Writing: Students will be given a number of opportunities to write in and outside the classroom. Through these writing tasks, students will practice using proper writing mechanics (including basic grammar), newly acquired vocabulary, and various sentence types. By the end of the semester, students will be able to write a cohesive stand- alone paragraph that includes a clear topic sentence, body explanations, and a concluding statement. "], ['Prerequisites', '| 1 이화 여자 대학교 INNOVATION EWHA '], ['Format', 'Lecture Discussion/Presentation Writing Other 30 % 30 % 30 % 10 % 4. Course '], ['Objectives', 'Detailed course objectives will be posted on Cyber Campus at the beginning of the semester. '], ['Evaluation', 'O Relative evaluation O Absolute evaluation O Others: Relative +Absolute Midterm Exam Final Exam Writing Oral Exam Participation Attendance 15% 15% 30% 10% 20% 10% Explanation of evaluation system II. Course Materials and Additional Readings 1. '], ['Required', 'Longshaw, R. & Blass, L. (2015). 21s Century Reading 1. Boston: Cengage Learning. '], ['Supplementary', 'Additional supplementary materials will be provided by the instructor. 3. Writing Assignments '], ['Description', 'One stand-alone paragraph. At least two drafts will be required. One stand-alone paragraph. At least two drafts will be required. Descriptive, Opinion, Process, Narrative, OR Definition (related to the themes of the set texts). 1 Descriptive, Opinion, Process, Narrative, OR Definition (related to the themes of the set texts). This will be different from Writing 1. 2 | 이화 여자 대학교 INNOVATION EWHA III. ']]

def summarzied_eng(word_list):
    summarzied_pages=[]
    for page in word_list :
        summarzied_word=[]
        if page[0] == 'Description':
            summarzied_word.append(page[0])
            summarzied_word.append(summarize(page[1]))
        else:
            summarzied_word.append(page[0])
            summarzied_word.append(page[1])
        summarzied_pages.append(summarzied_word)

    return summarzied_pages

print(summarzied_eng(word_list))
