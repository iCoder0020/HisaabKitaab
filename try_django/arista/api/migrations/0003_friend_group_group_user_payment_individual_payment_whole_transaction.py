# Generated by Django 3.0.3 on 2020-03-08 15:21

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('api', '0002_auto_20200308_1340'),
    ]

    operations = [
        migrations.CreateModel(
            name='Group',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('group_name', models.CharField(max_length=16)),
                ('simplified', models.BooleanField(default=False)),
            ],
        ),
        migrations.CreateModel(
            name='Payment_Whole',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('amount', models.FloatField()),
                ('timestamp', models.DateTimeField(auto_now_add=True)),
                ('description', models.CharField(max_length=128)),
                ('group', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Group')),
            ],
        ),
        migrations.CreateModel(
            name='Transaction',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('amount', models.FloatField()),
                ('borrower', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='Borrower', to=settings.AUTH_USER_MODEL)),
                ('lender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='Lender', to=settings.AUTH_USER_MODEL)),
                ('payment', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Payment_Whole')),
            ],
        ),
        migrations.CreateModel(
            name='Friend',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('group', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Group')),
                ('user1', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='User1', to=settings.AUTH_USER_MODEL)),
                ('user2', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='User2', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Payment_Individual',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('amount', models.FloatField()),
                ('lender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
                ('payment', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Payment_Whole')),
            ],
            options={
                'unique_together': {('payment', 'lender')},
            },
        ),
        migrations.CreateModel(
            name='Group_User',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('group', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Group')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'unique_together': {('user', 'group')},
            },
        ),
    ]
