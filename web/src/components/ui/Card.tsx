import React from 'react';
import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

export interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
  hoverEffect?: boolean;
}

export const Card: React.FC<CardProps> = ({
  className,
  hoverEffect = false,
  children,
  ...props
}) => {
  return (
    <div
      className={twMerge(
        clsx(
          'bg-slate-900/80 backdrop-blur-sm rounded-2xl border border-slate-800/80 shadow-xl transition-all duration-200 text-slate-100',
          hoverEffect && 'hover:shadow-2xl hover:border-slate-700 hover:bg-slate-900',
          className
        )
      )}
      {...props}
    >
      {children}
    </div>
  );
};
