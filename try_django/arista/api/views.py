from api.models import *
from api.serializers import *
from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

# '''
class UserList(APIView):

    def get(self, request):
        users = User.objects.all()
        serializer = UserSerializer(users, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
# '''


class Login(APIView):
    @staticmethod
    def get(request):

        x = request.data.get('user_name')
        y = request.data.get('password')
        print(x, y)
        if x is None:
            return Response('Bye')
        user = User.objects.get(user_name = x)

        serializer = UserSerializer(user)
        if user.password != y:
            return Response(serializer.data)
        else:
            return Response(serializer.data)

    def post(self, request):
        pass


