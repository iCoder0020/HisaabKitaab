from api.serializers import *
from rest_framework.views import APIView
from rest_framework import status
from django.http import JsonResponse
from django.contrib.auth import login, logout, authenticate
import json
# from rest_framework.permissions import IsAuthenticated


def error(message):
    response = {'error': message}
    return response


def ack(message):
    response = {'ack': message}
    return response


class AccountView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'login':
            username = request.data.get('username')
            password = request.data.get('password')

            if username is None or password is None:
                return JsonResponse(error("username and password are required"), status=status.HTTP_400_BAD_REQUEST)

            user = authenticate(username=username, password=password)

            if user is None:
                return JsonResponse(error("Wrong Credentials"), status=status.HTTP_406_NOT_ACCEPTABLE)
            else:
                login(request, user)
                return JsonResponse(ack("Successful Login"), status=status.HTTP_200_OK)

        elif action == 'logout':
            logout(request)
            return JsonResponse(ack("Successful Logout"), status=status.HTTP_200_OK)

        elif action == 'create':

            username = request.data.get('username')
            password = request.data.get('password')
            email = request.data.get('email')
            name = request.data.get('name')
            category = request.data.get('category')
            phone_number = request.data.get('phone_number')

            email_already_used = User.objects.filter(email=email).exists()
            if email_already_used:
                return JsonResponse({'message': "email already is use"}, status=status.HTTP_409_CONFLICT)

            username_already_used = User.objects.filter(username=username).exists()
            if username_already_used:
                return JsonResponse(error("username already in use"), status=status.HTTP_409_CONFLICT)

            user = User.objects.create_user(username=username, password=password, email=email)

            profile = Profile(user=user, name=name, phone_number=phone_number, category=category)
            profile.save()

            return JsonResponse(ack("account created"), status=status.HTTP_201_CREATED)

        elif action == 'delete':
            username = request.data.get('username')

            user = User.objects.get(username=username)
            user.delete()

            return JsonResponse(ack(username + " deleted"), status=status.HTTP_200_OK)

        else:
            return JsonResponse(error("invalid post in AccountView"), status=status.HTTP_400_BAD_REQUEST)


class GroupView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'groupname':
            groupid = request.data.get('groupid')

            group_exists = Group.objects.filter(pk=groupid).exists()

            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.get(pk=groupid)

            response = {'groupname': str(group)}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'groupid':
            groupname = request.data.get('groupname')

            group_exists = Group.objects.filter(group_name=groupname).exists()

            if group_exists is False:
                return JsonResponse(error("invalid groupname"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.filter(group_name=groupname)

            serializer = GroupSerializer(group, many=True)

            groups = [serializer.data[i]['id'] for i in range(len(serializer.data))]

            response = {'groups': groups}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'grouplist':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            group_user = Group_User.objects.filter(user=user)

            serializer = Group_UserSerializer(group_user, many=True)

            groups = [serializer.data[i]['group'] for i in range(len(serializer.data))]

            response = {'grouplist': groups}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'groupmembers':
            groupid = request.data.get('groupid')

            group_exists = Group.objects.filter(pk=groupid).exists()

            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.get(pk=groupid)
            group_user = Group_User.objects.filter(group=group)

            serializer = Group_UserSerializer(group_user, many=True)

            users = [serializer.data[i]['user'] for i in range(len(serializer.data))]

            return JsonResponse(users)

        elif action == 'create':
            userid = request.data.get('userid')
            groupname = request.data.get('groupname')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            group = Group(group_name=groupname, simplified=False)

            group.save()

            group_user = Group_User(group=group, user=user)
            group_user.save()

            return JsonResponse(ack(str(user) + " created " + str(group)), status=status.HTTP_200_OK)

        elif action == 'delete':
            groupid = request.data.get('groupid')

            group_exists = Group.objects.filter(pk=groupid).exists()

            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.get(pk=groupid)
            groupname = group.group_name
            group.delete()

            return JsonResponse(ack(groupname + " deleted"), status=status.HTTP_200_OK)

        elif action == 'addmember':
            groupid = request.data.get('groupid')
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()
            group_exists = Group.objects.filter(pk=groupid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)
            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            group = Group.objects.get(pk=groupid)

            group_user = Group_User.objects.filter(group=group, user=user)

            if group_user:
                return JsonResponse(ack('user already in group'), status=status.HTTP_200_OK)

            group_user = Group_User(group=group, user=user)
            group_user.save()

            return JsonResponse(ack(str(user) + " added to " + str(group)), status=status.HTTP_200_OK)

        elif action == 'deletemember':
            groupid = request.data.get('groupid')
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()
            group_exists = Group.objects.filter(pk=groupid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)
            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.get(pk=groupid)
            user = User.objects.get(pk=userid)

            group_user = Group_User.objects.filter(group=group, user=user)
            group_user.delete()

            return JsonResponse(ack(str(user) + " removed from " + str(group)), status=status.HTTP_200_OK)

        else:
            return JsonResponse(error('invalid put in GroupView'), status=status.HTTP_400_BAD_REQUEST)


class UserView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')
        if action == 'username':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists:
                user = User.objects.get(pk=userid)
                response = {'username': user.username}
                return JsonResponse(response, status=status.HTTP_200_OK)

            return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

        elif action == 'userid':
            username = request.data.get('username')

            user_exists = User.objects.filter(username=username).exists()

            if user_exists:
                user = User.objects.get(username=username)
                response = {'userid': user.id}
                return JsonResponse(response, status=status.HTTP_200_OK)

            return JsonResponse(error("invalid username"), status=status.HTTP_400_BAD_REQUEST)

        elif action == 'all':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists:
                user = User.objects.get(pk=userid)
                profile = Profile.objects.get(user=user)
                response = {'username': user.username, 'name': profile.name, 'email': user.email,
                            'phone_number': profile.phone_number, 'category': profile.category}
                return JsonResponse(response, status=status.HTTP_200_OK)

            return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

        else:
            return JsonResponse(error("invalid post in UserView"), status=status.HTTP_400_BAD_REQUEST)


class FriendView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'friendlist':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            friend = Friend.objects.filter(user1=user) | Friend.objects.filter(user2=user)

            serializer = FriendSerializer(friend, many=True)

            friends = [serializer.data[i]['user1'] if serializer.data[i]['user1'] != int(userid)
                       else serializer.data[i]['user2'] for i in range(len(serializer.data))]

            response = {'friends': friends}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'add':
            userid = request.data.get('userid')
            friendid = request.data.get('friendid')

            user_exists = User.objects.filter(pk=userid).exists()
            friend_exists = User.objects.filter(pk=friendid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)
            if friend_exists is False:
                return JsonResponse(error("invalid friendid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            friend = User.objects.get(pk=friendid)
            groupname = str(user) + str(friend)

            friendship_exists = ((Friend.objects.filter(user1=user) & Friend.objects.filter(user2=friend)) |
                                 (Friend.objects.filter(user1=friend) & Friend.objects.filter(user2=user))).exists()

            if friendship_exists:
                return JsonResponse(ack(str(user) + " and " + str(friend) + " are already friends"),
                                    status=status.HTTP_200_OK)

            group = Group(group_name=groupname, simplified=False)
            group.save()

            group_user1 = Group_User(group=group, user=user)
            group_user2 = Group_User(group=group, user=friend)
            group_user1.save()
            group_user2.save()

            friends = Friend(group=group, user1=user, user2=friend)
            friends.save()

            return JsonResponse(ack(str(user) + " is now friends with " + str(friend)), status=status.HTTP_200_OK)

        else:
            return JsonResponse(error('invalid post in FriendView'), status=status.HTTP_400_BAD_REQUEST)


class PaymentView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'get_transactions':
            paymentid = request.data.get('paymentid')

            payment_exists = Payment_Whole.objects.filter(pk=paymentid).exists()

            if payment_exists is False:
                return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)

            payment = Payment_Whole.objects.get(pk=paymentid)

            transaction = Transaction.objects.filter(payment=payment)

            serializer = TransactionSerializer(transaction, many=True)

            response = {'transactions': serializer.data}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'create':
            groupid = request.data.get('groupid')
            description = request.data.get('description')

            group_exists = Group.objects.filter(pk=groupid).exists()

            if group_exists is False:
                return JsonResponse(error("invalid groupid"), status=status.HTTP_400_BAD_REQUEST)

            group = Group.objects.get(pk=groupid)
            group_users = Group_User.objects.filter(group=group)
            users = [User.objects.get(pk=i.user.id) for i in group_users]
            group_size = len(users)

            payment_whole = Payment_Whole(group=group, amount=0, description=description)
            payment_whole.save()

            for i in range(group_size):
                payment_individual = Payment_Individual(payment=payment_whole, lender=users[i], amount=0)
                payment_individual.save()

            return JsonResponse(ack("Created payment_whole: " + str(payment_whole)), status=status.HTTP_200_OK)

        # elif action == 'add':
        #     userid = request.data.get('userid')
        #     paymentid = request.data.get('paymentid')
        #     amount = request.data.get('amount')
        #
        #     user_exists = User.objects.filter(pk=userid).exists()
        #     payment_exists = Payment_Whole.objects.filter(pk=paymentid).exists()
        #
        #     if user_exists is False:
        #         return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)
        #     if payment_exists is False:
        #         return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)
        #
        #     user = User.objects.get(pk=userid)
        #     payment_whole = Payment_Whole.objects.get(pk=paymentid)
        #     payment_whole.amount += int(amount)
        #
        #     payment_individual = Payment_Individual(payment=payment_whole, lender=user, amount=amount)
        #
        #     payment_individual.save()
        #     payment_whole.save()
        #
        #     return JsonResponse(ack("Added payment_individual: " + str(payment_individual)), status=status.HTTP_200_OK)

        elif action == 'update_description':
            paymentid = request.data.get('paymentid')
            description = request.data.get('description')

            payment_exists = Payment_Whole.objects.filter(pk=paymentid).exists()

            if payment_exists is False:
                return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)

            payment_whole = Payment_Whole.objects.get(pk=paymentid)

            payment_whole.description = description
            payment_whole.save()

            return JsonResponse(ack("Updated payment_whole: " + str(payment_whole)), status=status.HTTP_200_OK)

        elif action == 'update_individual':
            payment_individualid = request.data.get('payment_individualid')
            new_amount = request.data.get('amount')

            payment_individual_exists = Payment_Individual.objects.filter(pk=payment_individualid).exists()

            if payment_individual_exists is False:
                return JsonResponse(error("invalid payment_individualid"), status=status.HTTP_400_BAD_REQUEST)

            payment_individual = Payment_Individual.objects.get(pk=payment_individualid)
            old_amount = payment_individual.amount

            payment_whole = Payment_Whole.objects.get(pk=payment_individual.payment.id)
            payment_whole.amount -= int(old_amount)
            payment_whole.amount += int(new_amount)

            payment_individual.amount = new_amount

            payment_individual.save()
            payment_whole.save()

            return JsonResponse(ack("Updated payment_individual: " + str(payment_individual)),
                                status=status.HTTP_200_OK)

        elif action == 'create_transactions':
            paymentid = request.data.get('paymentid')

            payment_exists = Payment_Whole.objects.filter(pk=paymentid).exists()

            if payment_exists is False:
                return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)

            payment = Payment_Whole.objects.get(pk=paymentid)
            group = Group.objects.get(pk=payment.group.id)
            group_users = Group_User.objects.filter(group=group)
            users = [User.objects.get(pk=i.user.id) for i in group_users]
            group_size = len(users)
            payment_individual_query = Payment_Individual.objects.filter(payment=paymentid)
            equal_share = payment.amount / group_size
            to_pay = [equal_share for i in range(group_size)]
            paid = [payment_individual_query.filter(lender=u)[0].amount for u in users]
            will_get = [paid[i] - to_pay[i] for i in range(group_size)]

            prev_transactions = Transaction.objects.filter(payment=paymentid)
            for transaction in prev_transactions:
                transaction.delete()
            print(prev_transactions)
            while True:
                lenderidx = will_get.index(max(will_get))
                if will_get[lenderidx] == 0:
                    break
                while will_get[lenderidx] != 0:
                    borroweridx = will_get.index(min(filter(lambda i: i <= 0, will_get)))
                    if will_get[borroweridx] == 0:
                        will_get[lenderidx] = 0
                        break
                    amount = min(will_get[lenderidx], abs(will_get[borroweridx]))
                    will_get[lenderidx] -= amount
                    will_get[borroweridx] += amount
                    # print(str(users[lenderidx]) + " will get " + str(amount) + " from " + str(users[borroweridx]))
                    lender = users[lenderidx]
                    borrower = users[borroweridx]
                    transaction = Transaction(payment=payment, lender=lender, borrower=borrower, amount=amount)
                    transaction.save()

            return JsonResponse(ack("Created transactions for payment: " + str(payment)), status=status.HTTP_200_OK)

        elif action == 'delete':
            userid = request.data.get('userid')
            paymentid = request.data.get('paymentid')

            user_exists = User.objects.filter(pk=userid).exists()
            payment_exists = Payment_Whole.objects.filter(pk=paymentid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)
            if payment_exists is False:
                return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)
            payment_whole = Payment_Whole.objects.get(pk=paymentid)

            payment_individual = Payment_Individual.objects.filter(payment=payment_whole) \
                                 & Payment_Individual.objects.filter(lender=user)

            payment_individual.delete()

            return JsonResponse(ack("Deleted payment_individual: " + str(payment_individual)),
                                status=status.HTTP_200_OK)

        else:
            return JsonResponse(error("invalid post in PaymentView"), status=status.HTTP_400_BAD_REQUEST)


class Payment_UserView(APIView):

    @staticmethod
    def get(request):
        pass

    @staticmethod
    def post(request):
        action = request.data.get('type')

        if action == 'borrowed_from':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)

            payment = Payment_User.objects.filter(borrower=user)

            serializer = Payment_UserSerializer(payment, many=True)

            payments = serializer.data
            for i in payments:
                i['borrower_username'] = user.username
                lenderid = i['lender']
                i['lender_username'] = (User.objects.get(pk=lenderid)).username

            response = {'payments': payments}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'lended_to':
            userid = request.data.get('userid')

            user_exists = User.objects.filter(pk=userid).exists()

            if user_exists is False:
                return JsonResponse(error("invalid userid"), status=status.HTTP_400_BAD_REQUEST)

            user = User.objects.get(pk=userid)

            payment = Payment_User.objects.filter(lender=user)

            serializer = Payment_UserSerializer(payment, many=True)

            payments = serializer.data
            for i in payments:
                i['lender_username'] = user.username
                borrowerid = i['borrower']
                i['borrower_username'] = (User.objects.get(pk=borrowerid)).username

            response = {'payments': payments}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'pair':
            lenderid = request.data.get('lenderid')
            borrowerid = request.data.get('borrowerid')

            lender_exists = User.objects.filter(pk=lenderid).exists()
            borrower_exists = User.objects.filter(pk=borrowerid).exists()

            if lender_exists is False:
                return JsonResponse(error("invalid lenderid"), status=status.HTTP_400_BAD_REQUEST)
            if borrower_exists is False:
                return JsonResponse(error("invalid borrowerid"), status=status.HTTP_400_BAD_REQUEST)

            lender = User.objects.get(pk=lenderid)
            borrower = User.objects.get(pk=borrowerid)

            payment = Payment_User.objects.filter(lender=lender) & Payment_User.objects.filter(borrower=borrower)

            serializer = Payment_UserSerializer(payment, many=True)

            response = {'payments': serializer.data}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'create':
            description = request.data.get('description')
            total_amount = request.data.get('total_amount')
            lended_amount = request.data.get('lended_amount')
            borrowerid = request.data.get('borrowerid')
            lenderid = request.data.get('lenderid')
            payment_status = request.data.get('status')

            lender_exists = User.objects.filter(pk=lenderid).exists()
            borrower_exists = User.objects.filter(pk=borrowerid).exists()

            if lender_exists is False:
                return JsonResponse(error("invalid lenderid"), status=status.HTTP_400_BAD_REQUEST)
            if borrower_exists is False:
                return JsonResponse(error("invalid borrowerid"), status=status.HTTP_400_BAD_REQUEST)

            lender = User.objects.get(pk=lenderid)
            borrower = User.objects.get(pk=borrowerid)

            payment_user = Payment_User(description=description, total_amount=total_amount, lended_amount=lended_amount,
                                        lender=lender, borrower=borrower, status=payment_status)
            payment_user.save()

            response = {'paymentid': payment_user.id}
            return JsonResponse(response, status=status.HTTP_200_OK)

        elif action == 'update':
            paymentid = request.data.get('paymentid')
            description = request.data.get('description')
            total_amount = request.data.get('total_amount')
            lended_amount = request.data.get('lended_amount')
            lenderid = request.data.get('lenderid')
            borrowerid = request.data.get('borrowerid')
            payment_status = request.data.get('status')

            payment_exists = Payment_User.objects.filter(pk=paymentid).exists()

            if payment_exists is False:
                return JsonResponse(error("invalid paymentid"), status=status.HTTP_400_BAD_REQUEST)

            payment_user = Payment_User.objects.get(pk=paymentid)

            lender_exists = User.objects.filter(pk=lenderid).exists()
            borrower_exists = User.objects.filter(pk=borrowerid).exists()

            if lender_exists is False:
                return JsonResponse(error("invalid lenderid"), status=status.HTTP_400_BAD_REQUEST)
            if borrower_exists is False:
                return JsonResponse(error("invalid borrowerid"), status=status.HTTP_400_BAD_REQUEST)

            lender = User.objects.get(pk=lenderid)
            borrower = User.objects.get(pk=borrowerid)

            payment_user.lender = lender
            payment_user.borrower = borrower
            payment_user.description = description
            payment_user.total_amount = total_amount
            payment_user.lended_amount = lended_amount
            payment_user.status = payment_status

            payment_user.save()

            return JsonResponse(ack("Updated Payment_User: " + str(payment_user)), status=status.HTTP_200_OK)

        else:
            return JsonResponse(error("invalid post in Payment_UserView"), status=status.HTTP_400_BAD_REQUEST)
