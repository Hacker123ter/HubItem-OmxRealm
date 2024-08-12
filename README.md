## HubItem (OmxRealm)

![Version](https://img.shields.io/badge/Версия-1.0.1-blue.svg)
![API](https://img.shields.io/badge/Spigot%201.21%2B-blue.svg)

<h3 align="center">Discord: luckytsb</h3>

## ✨ Функции:

-️ :accessibility: Плагин, добавляющий автопредмет в хаб для открытия меню. Создано специально для сервера OmxRealm.

## 🚀 Установка:

- 😧 Скачайте <a href="https://github.com/Hacker123ter/HubItem-OmxRealm/raw/HubItem/target/HubItem-1.0.1-OmxRealm.jar" target="_blank">HubItem-1.0.1-OmxRealm.jar</a>.
- 🐈 Переместите его в папку "plugins" вашего сервера. (Убедитесь что Ядро и версия совместимы с плагином)
- 🪄 Перезапустите сервер.
- 😸 Радуйтесь жизни!

## 🎮 Использование:

0. После установки плагина появится папка HubItem (plugins/HubItem), в ней будет находится конфиг плагина (plugins/HubItem/config.yml)
1. Зайдите в конфиг с плагина и настройте значения:
```
material:
  slot:
  name: ""
  description:
    - ""
  command: ""
sound: ''
```
1.1. Что значит, каждая строчка и как настроить:
```
material: STONE ## Любой материал/предмет из 1.21 (должен быть написан капсом).
  slot: 0 ## Слот в инвентаре, в котором будет лежать предмет (хотбар слоты: 0-8).
  name: "Меню сервера" ## Название предмета (имеется полная поддержка hex color).
  description:
    - "Описание предмета, да" ## Описание у предмета (имеется полная поддержка hex color).
  command: "Menu" ## Команда, которая будет выполняться при использовании предмета в руке.
sound: 'ENTITY_PLAYER_LEVELUP' ## Звук, который будет проигрываться при использовании предмета.
```
1.2. Так же, стоит задержка перед частым использование предмета (2000 миллисекунд = 2 секунды = 40 тиков), адаптирована надпись.
2. После настройки config.yml перезагрузите сервер или пропишите "/hubitem reload" - Нужны права Op или hubitem.reload.
3. Обновите предмет в руке (через инвентарь попробуйте его взять).
4. Радуйтесь плагину и жизни!

## 🏗️ Команды:
```
/hubitem reload
```

## 🔒 Права:
```
hubitem.reload
default: op
```
