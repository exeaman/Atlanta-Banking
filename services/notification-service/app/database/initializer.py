from app.database.base import Base
from app.database.session import engine


def initialize_database():
    Base.metadata.drop_all(bind=engine)
    Base.metadata.create_all(bind=engine)