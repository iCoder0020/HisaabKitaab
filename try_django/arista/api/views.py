from api.serializers import *
from rest_framework.views import APIView
from rest_framework.response import Response
from django.contrib.auth import login, logout, authenticate


class AccountView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'login':
            username = request.data.get('username')
            password = request.data.get('password')

            if username is None or password is None:
                return Response("Missing Credentials")

            user = authenticate(username=username, password=password)

            if user is None:
                return Response("Wrong Credentials")
            else:
                login(request, user)
                return Response("Welcome " + str(user.first_name))

        elif action == 'logout':
            logout(request)
            return Response("Successfully logged out")

        else:
            return Response("invalid get in AccountView")

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'create':
            username = request.data.get('username')
            password = request.data.get('password')
            email = request.data.get('email')
            name = request.data.get('name')
            phone_number = request.data.get('phone_number')
            category = request.data.get('category')

            username_already_used = User.objects.filter(username=username).exists()
            if username_already_used:
                return Response("username already is use")

            email_already_used = User.objects.filter(email=email).exists()
            if email_already_used:
                return Response("email already in use")

            # ensure email is in goa.bits-pilani.ac.in domain

            user = User.objects.create_user(username=username, password=password, email=email)

            profile = Profile(user=user, name=name, phone_number=phone_number, category=category)
            profile.save()

            return Response("Account created: " + username)

        elif action == 'delete':
            username = request.data.get('username')

            user = User.objects.get(username=username)
            user.delete()

            return Response(username + " deleted")

        else:
            return Response("invalid post in AccountView")


class GroupView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'groupname':
            groupid = request.data.get('groupid')

            group = Group.objects.get(pk=groupid)

            return Response(str(group))

        elif action == 'groupid':
            groupname = request.data.get('groupname')

            group = Group.objects.filter(group_name=groupname)

            serializer = GroupSerializer(group, many=True)

            groups = [serializer.data[i]['id'] for i in range(len(serializer.data))]

            return Response(groups)

        elif action == 'grouplist':
            userid = request.data.get('userid')

            user = User.objects.get(pk=userid)
            group_user = Group_User.objects.filter(user=user)

            serializer = Group_UserSerializer(group_user, many=True)

            groups = [serializer.data[i]['group'] for i in range(len(serializer.data))]

            return Response(groups)

        elif action == 'groupmembers':
            groupid = request.data.get('groupid')

            group = Group.objects.get(pk=groupid)
            group_user = Group_User.objects.filter(group=group)

            serializer = Group_UserSerializer(group_user, many=True)

            users = [serializer.data[i]['user'] for i in range(len(serializer.data))]

            return Response(users)

        else:
            return Response('invalid get in GroupView')

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'create':
            userid = request.data.get('userid')
            groupname = request.data.get('groupname')

            user = User.objects.get(pk=userid)
            group = Group(group_name=groupname, simplified=False)

            group.save()

            group_user = Group_User(group=group, user=user)
            group_user.save()

            return Response(str(user) + " created " + str(group))

        elif action == 'delete':
            groupid = request.data.get('groupid')

            group = Group.objects.get(pk=groupid)
            groupname = group.group_name
            group.delete()

            return Response(groupname + " deleted")

        elif action == 'addmember':
            groupid = request.data.get('groupid')
            userid = request.data.get('userid')

            group = Group.objects.get(pk=groupid)
            user = User.objects.get(pk=userid)

            group_user = Group_User.objects.filter(group=group, user=user)

            if group_user:
                return Response('user already in group')

            group_user = Group_User(group=group, user=user)
            group_user.save()

            return Response(str(user) + " added to " + str(group))

        elif action == 'deletemember':
            groupid = request.data.get('groupid')
            userid = request.data.get('userid')

            group = Group.objects.get(pk=groupid)
            user = User.objects.get(pk=userid)

            group_user = Group_User.objects.filter(group=group, user=user)
            group_user.delete()

            return Response(str(user) + " removed from " + str(group))

        else:
            return Response('invalid put in GroupView')


class UserView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'username':
            userid = request.data.get('userid')

            user = User.objects.get(pk=userid)

            return Response(str(user))

        elif action == 'userid':
            username = request.data.get('username')

            user_exists = User.objects.filter(username=username).exists()

            if user_exists:
                user = User.objects.get(username=username)
                return Response(user.id)

            return Response('invalid username')

        else:
            return Response('invalid get in UserView')

    @staticmethod
    def post(request):
        pass


class FriendView(APIView):

    @staticmethod
    def get(request):
        action = request.data.get('type')

        if action == 'friendlist':
            userid = request.data.get('userid')

            user = User.objects.get(pk=userid)
            friend = Friend.objects.filter(user1=user) | Friend.objects.filter(user2=user)

            serializer = FriendSerializer(friend, many=True)

            friends = [serializer.data[i]['user1'] if serializer.data[i]['user1'] != int(userid)
                       else serializer.data[i]['user2'] for i in range(len(serializer.data))]

            return Response(friends)

        else:
            return Response('invalid get in FriendView')

    @staticmethod
    def post(request):
        pass


class PaymentView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'create':
            groupid = request.data.get('groupid')
            description = request.data.get('description')

            group = Group.objects.get(pk=groupid)

            payment_whole = Payment_Whole(group=group, amount=0, description=description)

            payment_whole.save()

            return Response("Created payment_whole: " + str(payment_whole))

        elif action == 'add':
            userid = request.data.get('userid')
            paymentid = request.data.get('paymentid')
            amount = request.data.get('amount')

            user = User.objects.get(pk=userid)
            payment_whole = Payment_Whole.objects.get(pk=paymentid)
            payment_whole.amount += int(amount)

            payment_individual = Payment_Individual(payment=payment_whole, lender=user, amount=amount)

            payment_individual.save()
            payment_whole.save()

            return Response("Added payment_individual: " + str(payment_individual))

        else:
            return Response("invalid post in PaymentView")