import pymysql
import pymysql.cursors
import logging

class Database:
    '''
    데이터베이스 제어 모듈
    '''

    def __init__(self, host, user, password, db_name, charset = 'utf8'):
        self.host = host
        self.user = user
        self.password = password
        self.charset = charset
        self.db_name = db_name
        self.conn = None

    #db 연결
    def connect(self):
        if self.conn != None:
            return

        self.conn = pymysql.connect(
            host= self.host,
            user= self.user,
            password= self.password,
            db = self.db_name,
            charset=self.charset
        )

    #db 연결 닫기
    def close(self):
        if self.conn is None:
            return
        if not self.conn.open:
            self.conn = None

    #SQL 구문 실행
    def execute(self, sql):
        last_row_id = -1
        try :
            with self.conn.cursor() as cursor:
                cursor.execute(sql)
            self.conn.commit()
            last_row_id = cursor.lastrowid
        except Exception as ex:
            logging.error(ex)

        finally:
            return last_row_id

    #SELECT 구문 시행
    def select_one(self, sql):
        result = None

        try:
            with self.conn.cursor(pymysql.cursors.DictCursor) as cursor:
                cursor.execute(sql)
                result = cursor.fetchone()  #단 1개의 데이터 row만 불러온다
        except Exception as ex:
            logging.error(ex)
        finally:
            return result

    #SELECT 구문 실행 후 전체 데이터 row 불러오기
    def select_all(self, sql):
        result = None
        try :
            with self.conn.cursor(pymysql.cursors.DictCursor) as cursor:
                cursor.execute(sql)
                result = cursor.fetchall()
        except Exception as ex:
            logging.error()

        finally:
            return result