ALTER TABLE public.note
    ALTER COLUMN date TYPE TIMESTAMP,
    ALTER COLUMN date SET DEFAULT CURRENT_TIMESTAMP;