# Generated by Django 3.0.3 on 2020-02-20 13:53

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_auto_20200220_1328'),
    ]

    operations = [
        migrations.CreateModel(
            name='Payment_Whole',
            fields=[
                ('payment_id', models.AutoField(primary_key=True, serialize=False)),
                ('amount', models.FloatField()),
                ('timestamp', models.DateTimeField()),
                ('description', models.TextField()),
                ('group', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Group')),
            ],
        ),
        migrations.CreateModel(
            name='Transaction',
            fields=[
                ('transaction_id', models.AutoField(primary_key=True, serialize=False)),
                ('amount', models.FloatField()),
                ('borrower', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='Borrower', to='api.User')),
                ('lender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='Lender', to='api.User')),
                ('payment', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Payment_Whole')),
            ],
        ),
        migrations.CreateModel(
            name='Friends',
            fields=[
                ('group', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, primary_key=True, serialize=False, to='api.Group')),
                ('user1', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='User1', to='api.User')),
                ('user2', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='User2', to='api.User')),
            ],
        ),
        migrations.CreateModel(
            name='Payment_Individual',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('amount', models.FloatField()),
                ('lender', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.User')),
                ('payment', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Payment_Whole')),
            ],
            options={
                'unique_together': {('payment', 'lender')},
            },
        ),
    ]
