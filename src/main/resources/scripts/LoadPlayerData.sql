-- i will add scripts later if somebody reminds me
INSERT INTO player_data (
    player_uuid,
    receive_messages,
    message_sounds,
    duel_requests,
    duel_sounds,
    allow_spectators,
    score_board,
    global_chat,
    player_weather,
    player_time,
    ping_range,
    win_streak,
    kit_datas
)
VALUES (
    ?, ?, ?, ?, ?, ?, ?, ?, ?::weather_type, ?, ?, ?, ?::jsonb
)
ON CONFLICT (player_uuid) DO UPDATE SET
    receive_messages = EXCLUDED.receive_messages,
    message_sounds = EXCLUDED.message_sounds,
    duel_requests = EXCLUDED.duel_requests,
    duel_sounds = EXCLUDED.duel_sounds,
    allow_spectators = EXCLUDED.allow_spectators,
    score_board = EXCLUDED.score_board,
    global_chat = EXCLUDED.global_chat,
    player_weather = EXCLUDED.player_weather,
    player_time = EXCLUDED.player_time,
    ping_range = EXCLUDED.ping_range,
    win_streak = EXCLUDED.win_streak,
    kit_datas = EXCLUDED.kit_datas;