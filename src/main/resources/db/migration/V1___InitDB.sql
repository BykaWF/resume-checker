CREATE TABLE resume (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    resume_text TEXT,
    uploaded_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE cover_letter (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    letter_text TEXT,
    uploaded_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);
