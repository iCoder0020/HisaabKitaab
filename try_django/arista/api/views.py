from django.http import HttpResponse


# Create your views here.
def my_view(request):
    if request.method == 'GET':
        